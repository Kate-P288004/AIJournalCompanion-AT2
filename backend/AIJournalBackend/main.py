from fastapi import FastAPI

app = FastAPI(title="AI Journal Companion API")

@app.get("/")
def root():
    return {"status": "API running"}

@app.get("/health")
def health():
    return {"health": "ok"}
