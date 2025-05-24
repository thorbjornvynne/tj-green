
from sqlalchemy import Column, Integer, String, Boolean, DateTime, Float, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.sql import func

Base = declarative_base()

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    username = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

class NewsRecord(Base):
    __tablename__ = "news"
    id = Column(Integer, primary_key=True)
    title = Column(String)
    content = Column(String)
    ticker = Column(String)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

class PredictionRecord(Base):
    account_id = Column(Integer, nullable=True)
    tweet_text = Column(String, nullable=True)
    __tablename__ = "predictions"
    id = Column(Integer, primary_key=True)
    news_id = Column(Integer)
    stock_id = Column(Integer, nullable=True)
    sentiment = Column(String)
    prediction = Column(String)
    confidence = Column(Float)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

class MonitoredAccount(Base):
    __tablename__ = "accounts"
    id = Column(Integer, primary_key=True)
    username = Column(String, unique=True, index=True)
    enabled = Column(Boolean, default=True)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

class MonitoredStock(Base):
    __tablename__ = "stocks"
    id = Column(Integer, primary_key=True)
    ticker = Column(String, unique=True, index=True)
    name = Column(String)
    enabled = Column(Boolean, default=True)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

    
        evaluation_time = Column(DateTime(timezone=True), nullable=True)
        actual_movement = Column(String, nullable=True)
        is_correct = Column(Boolean, nullable=True)
    