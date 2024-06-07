from fastapi import APIRouter, Depends, HTTPException, status
from services.model_service import ModelService
from utils import get_model_service
from models import PredictionRequest

model_router = APIRouter()

@model_router.get("/")
async def read_root():
    pass

@model_router.post("/predict")
async def predict(request: PredictionRequest, service: ModelService = Depends(get_model_service)):
    prediction = service.predict(request.params.path)
    return prediction