
from fastapi import APIRouter, HTTPException
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import MonitoredStock
from app.models.schemas import StockCreate, StockInfo
from typing import List

stock_router = APIRouter()

@stock_router.get("/stocks", response_model=List[StockInfo])
def list_stocks():
    db: Session = SessionLocal()
    stocks = db.query(MonitoredStock).all()
    db.close()
    return stocks

@stock_router.post("/stocks", response_model=StockInfo)
def add_stock(stock: StockCreate):
    db: Session = SessionLocal()
    if db.query(MonitoredStock).filter_by(ticker=stock.ticker.upper()).first():
        db.close()
        raise HTTPException(status_code=400, detail="Stock already exists")
    entry = MonitoredStock(ticker=stock.ticker.upper(), name=stock.name)
    db.add(entry)
    db.commit()
    db.refresh(entry)
    db.close()
    return entry

@stock_router.delete("/stocks/{ticker}")
def delete_stock(ticker: str):
    db: Session = SessionLocal()
    stock = db.query(MonitoredStock).filter_by(ticker=ticker.upper()).first()
    if not stock:
        db.close()
        raise HTTPException(status_code=404, detail="Stock not found")
    db.delete(stock)
    db.commit()
    db.close()
    return {"status": "deleted", "ticker": ticker}
