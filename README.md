# TJ-Green

**TJ-Green** is an AI-driven Android application and FastAPI backend for predicting the market impact of real-time news and social media events on stocks.

---

## 📦 Project Structure

```
.
├── backend/                  # FastAPI backend
│   ├── main.py
│   └── app/
│       ├── api/             # API endpoints
│       ├── auth/            # Auth & security logic
│       ├── core/            # Config & utilities
│       ├── models/          # Pydantic schemas
│       ├── services/        # Business logic (predictions, evaluations)
│       └── stocks/          # Stock-specific modules
│
├── android_app/             # Android client
│   ├── app/
│   │   └── src/main/java/com/example/stockpredictor/
│   │       ├── ui/          # Feature-based Activities/Fragments
│   │       ├── data/        # Repositories, DTOs
│   │       ├── network/     # API clients
│   │       ├── service/     # WebSocket client
│   │       └── util/        # Notification helper, settings
│   ├── gradlew              # Gradle wrapper
│   ├── settings.gradle
│   ├── build.gradle         # Project-level
│   └── gradle/              # Gradle wrapper configs
```

---

## 🧠 Features

- 📲 Android client with biometric login, news & stock feed, WebSocket alerts
- 🌐 FastAPI backend with modular structure
- 🧠 GPT-4 powered sentiment analysis and prediction
- 📈 Evaluation of prediction accuracy (tweets and news)
- 🔔 Real-time push notifications via WebSockets
- 🎨 Dynamic theming with downloadable themes

---

## 🚀 Backend Setup

### 1. Requirements
- Python 3.9+
- Install dependencies:
  ```bash
  pip install -r requirements.txt
  ```

### 2. Running Locally

```bash
cd backend
uvicorn main:app --reload
```

### 3. WebSocket Alerts

When a high-confidence prediction is made, alerts are broadcast over `/ws/alerts`.

---

## 🗄 PostgreSQL Integration

This project uses a PostgreSQL container alongside the backend.

### Environment Variables (`.env`)

Example:

```env
DATABASE_URL=postgresql://tjuser:tjpass@db:5432/tjgreen
POSTGRES_DB=tjgreen
POSTGRES_USER=tjuser
POSTGRES_PASSWORD=tjpass
```

---

## 🐳 Docker Deployment

### Build & Run the Backend

```bash
cd backend
cp .env.example .env
docker-compose up --build
```

- Access API at `http://localhost:8000`
- PostgreSQL DB is automatically provisioned in a named volume `pgdata`

To stop:
```bash
docker-compose down
```

---

## 📱 Android Build & Run

### 1. Requirements
- Android Studio / SDK
- JDK 11+

### 2. Command-Line Build

```bash
cd android_app
./gradlew assembleDebug
```

### 3. Install APK

After build:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔐 Configuration

### Backend
Use `.env` or `config.py` under `app/core/` to set:
- OpenAI API key
- Database URL
- Scheduler intervals

### Android
Tokens are securely stored with EncryptedSharedPreferences. Base URLs can be configured via `AppConfig.kt`.

---

## 🧪 Future Work
- CI/CD via GitHub Actions
- Admin dashboard & analytics
- Theme palette extraction from images
- Unit & integration tests (backend and client)

---

## 🧑‍💻 Author
Developed by [Thorbjørn Vynne](https://github.com/thorbjornvynne)

---

## 📄 License
MIT