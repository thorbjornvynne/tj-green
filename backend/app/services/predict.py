
import random

def basic_nlp_predict(title: str, content: str):
    # Placeholder: basic rule-based or sentiment mock
    keywords = ["growth", "profit", "beats", "record", "surge", "upgrade"]
    negative = ["loss", "decline", "cut", "downgrade", "drop", "miss"]
    score = 0
    text = title.lower() + " " + content.lower()
    for word in keywords:
        if word in text:
            score += 1
    for word in negative:
        if word in text:
            score -= 1
    sentiment = "positive" if score > 0 else "negative" if score < 0 else "neutral"
    prediction = "up" if score > 0 else "down" if score < 0 else "neutral"
    confidence = min(1.0, abs(score) / 5 + 0.4)
    return sentiment, prediction, round(confidence, 2)

def gpt_predict(title: str, content: str):
    # Placeholder: simulate GPT call
    options = [("positive", "up", 0.92), ("negative", "down", 0.87), ("neutral", "neutral", 0.70)]
    return random.choice(options)


# Real GPT-4 prediction logic below

import os
import openai
from dotenv import load_dotenv

load_dotenv()

openai.api_key = os.getenv("OPENAI_API_KEY")

def gpt_predict(title: str, content: str):
    prompt = f"Analyze the following news and predict the likely impact on the associated stock.\n\nTitle: {title}\nContent: {content}\n\nRespond with: Sentiment (positive, neutral, negative), Prediction (up, down, neutral), Confidence (0-1)."

    try:
        response = openai.ChatCompletion.create(
            model="gpt-4",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.4
        )
        text = response.choices[0].message.content.strip().lower()

        sentiment = "neutral"
        prediction = "neutral"
        confidence = 0.7

        if "positive" in text:
            sentiment = "positive"
        elif "negative" in text:
            sentiment = "negative"

        if "up" in text:
            prediction = "up"
        elif "down" in text:
            prediction = "down"

        for part in text.split():
            try:
                num = float(part)
                if 0 <= num <= 1:
                    confidence = num
                    break
            except:
                continue

        return sentiment, prediction, round(confidence, 2)

    except Exception as e:
        print("GPT Error:", e)
        return "neutral", "neutral", 0.6
