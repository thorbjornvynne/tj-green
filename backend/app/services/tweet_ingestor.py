
import os
import requests
from dotenv import load_dotenv
from app.models.database import SessionLocal
from app.models.entities import MonitoredAccount
from app.services.predict import basic_nlp_predict, gpt_predict

load_dotenv()

X_BEARER_TOKEN = os.getenv("X_BEARER_TOKEN")
TWITTER_API_URL = "https://api.twitter.com/2"

HEADERS = {"Authorization": f"Bearer {X_BEARER_TOKEN}"}

def fetch_tweets(username, max_results=5):
    url = f"{TWITTER_API_URL}/tweets/search/recent?query=from:{username}&max_results={max_results}"
    try:
        response = requests.get(url, headers=HEADERS)
        if response.status_code == 200:
            tweets = response.json().get("data", [])
            return [tweet["text"] for tweet in tweets]
        else:
            print("Twitter API error:", response.status_code, response.text)
    except Exception as e:
        print("Error fetching tweets:", e)
    return []

    db = SessionLocal()
    accounts = db.query(MonitoredAccount).filter_by(enabled=True).all()
    for account in accounts:
        tweets = fetch_tweets(account.username)
        for tweet in tweets:
            print(f"[Tweet from @{account.username}]: {tweet[:80]}...")
            sentiment, prediction, confidence = gpt_predict("Tweet", tweet)
            print(f"→ Sentiment: {sentiment}, Prediction: {prediction}, Confidence: {confidence}")
    db.close()

from app.models.entities import PredictionRecord

def run_tweet_ingestion():
    db = SessionLocal()
    accounts = db.query(MonitoredAccount).filter_by(enabled=True).all()
    for account in accounts:
        tweets = fetch_tweets(account.username)
        for tweet in tweets:
            print(f"[Tweet from @{account.username}]: {tweet[:80]}...")
            sentiment, prediction, confidence = gpt_predict("Tweet", tweet)
            print(f"→ Sentiment: {sentiment}, Prediction: {prediction}, Confidence: {confidence}")

            record = PredictionRecord(
                account_id=account.id,
                tweet_text=tweet,
                sentiment=sentiment,
                prediction=prediction,
                confidence=confidence
            )
            db.add(record)
    db.commit()
    db.close()
