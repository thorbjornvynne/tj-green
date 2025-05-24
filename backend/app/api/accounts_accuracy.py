
from fastapi import APIRouter
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import MonitoredAccount, PredictionRecord

router = APIRouter()

@router.get("/accounts/accuracy/all")
def all_account_accuracy():
    db: Session = SessionLocal()
    accounts = db.query(MonitoredAccount).all()
    result = []

    for account in accounts:
        preds = db.query(PredictionRecord).filter_by(account_id=account.id).all()
        high_conf = [p for p in preds if p.confidence >= 0.8]
        correct = [p for p in high_conf if p.is_correct is True]
        accuracy = len(correct) / len(high_conf) if high_conf else 0.0

        result.append({
            "username": account.username,
            "total_predictions": len(preds),
            "high_confidence": len(high_conf),
            "accuracy": round(accuracy, 2)
        })

    db.close()
    return result
