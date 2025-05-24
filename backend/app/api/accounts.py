
from fastapi import APIRouter, HTTPException
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import MonitoredAccount
from app.models.schemas import AccountCreate, AccountInfo
from typing import List

account_router = APIRouter()

@account_router.get("/accounts", response_model=List[AccountInfo])
def list_accounts():
    db: Session = SessionLocal()
    accounts = db.query(MonitoredAccount).all()
    db.close()
    return accounts

@account_router.post("/accounts", response_model=AccountInfo)
def add_account(account: AccountCreate):
    db: Session = SessionLocal()
    if db.query(MonitoredAccount).filter_by(username=account.username).first():
        db.close()
        raise HTTPException(status_code=400, detail="Account already exists")
    entry = MonitoredAccount(username=account.username)
    db.add(entry)
    db.commit()
    db.refresh(entry)
    db.close()
    return entry

@account_router.delete("/accounts/{username}")
def delete_account(username: str):
    db: Session = SessionLocal()
    account = db.query(MonitoredAccount).filter_by(username=username).first()
    if not account:
        db.close()
        raise HTTPException(status_code=404, detail="Account not found")
    db.delete(account)
    db.commit()
    db.close()
    return {"status": "deleted", "username": username}
