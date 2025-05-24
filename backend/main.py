
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.auth import auth_router
from app.api.predict import predict_router
from app.api.stocks import stock_router
from app.api.accounts import account_router
from app.api.stock_detail import detail_router
from app.services.scheduler import run_scheduler

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(auth_router)
app.include_router(predict_router)
app.include_router(stock_router)
app.include_router(account_router)
app.include_router(detail_router)

run_scheduler()
