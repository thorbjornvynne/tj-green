
import time
from threading import Thread

def run_scheduler(interval_seconds=300):
    def task():
        while True:
            print("Running scheduled fetch...")
            # Call tweet/news scraping here
            time.sleep(interval_seconds)
    Thread(target=task, daemon=True).start()

from app.services.tweet_ingestor import run_tweet_ingestion
from app.services.evaluator import evaluate_predictions

def run_scheduler(interval_seconds=300):
    def task():
        while True:
            print("Running scheduled fetch...")
            run_tweet_ingestion()
            evaluate_predictions()
            time.sleep(interval_seconds)
    Thread(target=task, daemon=True).start()
