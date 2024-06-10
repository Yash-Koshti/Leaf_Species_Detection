from fastapi import APIRouter, Depends, HTTPException, status
from models import Prediction, PredictionRequest, PredictionResponse
from services.model_service import ModelService
from utils import get_model_service

model_router = APIRouter()


@model_router.get("/")
async def read_root():
    pass


@model_router.post("/predict")
async def predict(
    request: PredictionRequest, service: ModelService = Depends(get_model_service)
) -> PredictionResponse[list[Prediction]]:
    predictions = await service.predict(request.params.path)
    if len(predictions) > 0:
        return PredictionResponse(
            code=status.HTTP_200_OK,
            status="success",
            message="Successfully predicted",
            result=predictions,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_204_NO_CONTENT,
            detail="Failed to predict",
        )
