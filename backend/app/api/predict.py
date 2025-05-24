
from fastapi import APIRouter
from app.models.schemas import NewsItem, PredictionResponse
from app.services.predict import basic_nlp_predict, gpt_predict

predict_router = APIRouter()

@predict_router.post("/predict", response_model=PredictionResponse)
def predict(news: NewsItem):
    if news.use_gpt:
        sentiment, prediction, confidence = gpt_predict(news.title, news.content)
    else:
        sentiment, prediction, confidence = basic_nlp_predict(news.title, news.content)
    
    from app.models.database import SessionLocal
    from app.models.entities import PredictionRecord
    db = SessionLocal()
    record = PredictionRecord(
        ticker=news.ticker,
        sentiment=sentiment,
        prediction=prediction,
        confidence=confidence
    )
    db.add(record)
    db.commit()
    db.close()
    return {"sentiment": sentiment, "prediction": prediction, "confidence": confidence}
    
