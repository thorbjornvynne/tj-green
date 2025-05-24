
from fastapi import APIRouter, HTTPException
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import MonitoredAccount, PredictionRecord

accuracy_router = APIRouter()

@accuracy_router.get("/accounts/{username}/accuracy")
def account_accuracy(username: str):
    db: Session = SessionLocal()
    account = db.query(MonitoredAccount).filter_by(username=username).first()
    if not account:
        db.close()
        raise HTTPException(status_code=404, detail="Account not found")

    preds = db.query(PredictionRecord).filter_by(account_id=account.id).all()
    db.close()

    high_conf = [p for p in preds if p.confidence >= 0.8]
    total = len(preds)
    correct = sum(1 for p in high_conf if "correct" in (p.prediction or ""))  # placeholder logic
    accuracy = correct / len(high_conf) if high_conf else 0.0

    return {
        "account": username,
        "total_predictions": total,
        "high_confidence": len(high_conf),
        "accuracy": round(accuracy, 2),
        "note": "Accuracy logic is placeholder until stock data is linked"
    }
