
from fastapi import APIRouter, HTTPException
from sqlalchemy.orm import Session
from app.models.database import SessionLocal
from app.models.entities import User
from app.models.schemas import UserCreate, Token
from app.auth.utils import hash_password, verify_password, create_access_token

auth_router = APIRouter()

@auth_router.post("/register", response_model=Token)
def register(user: UserCreate):
    db: Session = SessionLocal()
    if db.query(User).filter_by(username=user.username).first():
        db.close()
        raise HTTPException(status_code=400, detail="Username already taken")
    db_user = User(username=user.username, hashed_password=hash_password(user.password))
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    db.close()
    token = create_access_token({"sub": user.username})
    return {"access_token": token, "token_type": "bearer"}

@auth_router.post("/login", response_model=Token)
def login(user: UserCreate):
    db: Session = SessionLocal()
    db_user = db.query(User).filter_by(username=user.username).first()
    if not db_user or not verify_password(user.password, db_user.hashed_password):
        db.close()
        raise HTTPException(status_code=401, detail="Invalid credentials")
    db.close()
    token = create_access_token({"sub": user.username})
    return {"access_token": token, "token_type": "bearer"}
