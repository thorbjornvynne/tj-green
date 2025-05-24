
from fastapi import APIRouter, HTTPException
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import MonitoredStock, PredictionRecord
from datetime import datetime, timedelta
import yfinance as yf

chart_router = APIRouter()

@chart_router.get("/stocks/{ticker}/chart")
def stock_price_chart(ticker: str):
    db: Session = SessionLocal()
    stock = db.query(MonitoredStock).filter_by(ticker=ticker.upper()).first()
    if not stock:
        db.close()
        raise HTTPException(status_code=404, detail="Stock not found")

    now = datetime.utcnow()
    start = now - timedelta(days=7)
    hist = yf.Ticker(ticker).history(start=start.strftime("%Y-%m-%d"), end=now.strftime("%Y-%m-%d"))

    preds = db.query(PredictionRecord).filter_by(ticker=ticker.upper()).all()
    db.close()

    chart_data = {
        "price_series": [{"date": t.strftime("%Y-%m-%d"), "price": float(p)} for t, p in hist["Close"].items()],
        "predictions": [{
            "time": p.created_at.isoformat(),
            "prediction": p.prediction,
            "confidence": p.confidence,
            "is_correct": p.is_correct
        } for p in preds]
    }
    return chart_data
