
FROM python:3.10

WORKDIR /app
COPY ./backend /app

RUN pip install --upgrade pip && \
    pip install fastapi uvicorn[standard] sqlalchemy passlib[bcrypt] python-jose

EXPOSE 8000
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
