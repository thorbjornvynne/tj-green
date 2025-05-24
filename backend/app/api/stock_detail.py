
from fastapi import APIRouter, HTTPException
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import MonitoredStock, PredictionRecord
from typing import List

detail_router = APIRouter()

@detail_router.get("/stocks/{ticker}/history")
def stock_prediction_history(ticker: str):
    db: Session = SessionLocal()
    stock = db.query(MonitoredStock).filter_by(ticker=ticker.upper()).first()
    if not stock:
        db.close()
        raise HTTPException(status_code=404, detail="Stock not found")

    preds = db.query(PredictionRecord).filter_by(stock_id=stock.id).all()
    db.close()

    correct = sum(1 for p in preds if p.confidence >= 0.8)
    accuracy = correct / len(preds) if preds else 0.0
    return {
        "ticker": stock.ticker,
        "name": stock.name,
        "total_predictions": len(preds),
        "high_confidence": correct,
        "reliability_score": round(accuracy, 2),
        "history": [{
            "sentiment": p.sentiment,
            "prediction": p.prediction,
            "confidence": p.confidence,
            "time": p.created_at
        } for p in preds]
    }
