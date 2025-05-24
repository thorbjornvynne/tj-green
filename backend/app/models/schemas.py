
from pydantic import BaseModel
from typing import List, Optional

class UserCreate(BaseModel):
    username: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class NewsItem(BaseModel):
    title: str
    content: str
    ticker: str
    use_gpt: Optional[bool] = True

class PredictionResponse(BaseModel):
    sentiment: str
    prediction: str
    confidence: float

class StockCreate(BaseModel):
    ticker: str
    name: str

class StockInfo(BaseModel):
    id: int
    ticker: str
    name: str
    enabled: bool

    class Config:
        orm_mode = True

class AccountCreate(BaseModel):
    username: str

class AccountInfo(BaseModel):
    id: int
    username: str
    enabled: bool

    class Config:
        orm_mode = True
