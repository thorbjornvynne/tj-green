
import yfinance as yf
from datetime import datetime, timedelta
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import PredictionRecord
from dateutil import parser

def determine_direction(start_price, end_price, threshold=0.01):
    change = (end_price - start_price) / start_price
    if change > threshold:
        return "up"
    elif change < -threshold:
        return "down"
    else:
        return "neutral"

def evaluate_predictions():
    db: Session = SessionLocal()
    cutoff = datetime.utcnow() - timedelta(days=1)

    preds = db.query(PredictionRecord).filter(
        PredictionRecord.created_at < cutoff,
        PredictionRecord.actual_movement == None
    ).all()

    for pred in preds:
        if not pred.ticker:
            continue

        try:
            stock = yf.Ticker(pred.ticker)
            t0 = pred.created_at
            t1 = t0 + timedelta(hours=24)

            hist = stock.history(start=t0.strftime("%Y-%m-%d"), end=(t1 + timedelta(days=1)).strftime("%Y-%m-%d"))
            if hist.empty:
                continue

            prices = hist["Close"]
            t0_price = prices.iloc[0]
            t1_price = prices.iloc[-1]

            actual = determine_direction(t0_price, t1_price)
            is_correct = actual == pred.prediction

            pred.actual_movement = actual
            pred.is_correct = is_correct
            pred.evaluation_time = datetime.utcnow()
            print(f"Evaluated {pred.ticker}: {actual}, correct: {is_correct}")
        except Exception as e:
            print("Eval error:", e)

    db.commit()
    db.close()
