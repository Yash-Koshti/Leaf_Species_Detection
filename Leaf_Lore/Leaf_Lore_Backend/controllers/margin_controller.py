from fastapi import APIRouter, Depends, HTTPException
from models import Margin, MarginRequest, MarginResponse
from services.margin_service import MarginService
from utils import get_margin_service

margin_router = APIRouter()


@margin_router.get("/")
async def read_root():
    return "Available endpoints: GET /all_margins  POST /create_margin  GET /get_by_margin_id  DELETE /delete_margin"


@margin_router.get("/all_margins", response_model=MarginResponse[list[Margin]])
async def get_all_margins(
    service: MarginService = Depends(get_margin_service),
) -> MarginResponse[list[Margin]] | HTTPException:
    margins = service.get_all_margins()
    if margins:
        return MarginResponse(
            code=200,
            status="Ok",
            message="Margins fetched successfully",
            result=margins,
        )
    else:
        raise HTTPException(status_code=404, detail="No margins found!")


@margin_router.post("/create_margin", response_model=MarginResponse[Margin])
async def create_margin(
    request: MarginRequest,
    service: MarginService = Depends(get_margin_service),
) -> MarginResponse[Margin] | HTTPException:
    margin = service.create_margin(request.params)
    if margin:
        return MarginResponse(
            code=200,
            status="Ok",
            message="Margin created successfully",
            result=margin,
        )
    else:
        raise HTTPException(status_code=500, detail="Internal Server Error!")


@margin_router.get("/get_by_margin_id", response_model=MarginResponse[Margin])
async def get_by_margin_id(
    request: MarginRequest,
    service: MarginService = Depends(get_margin_service),
) -> MarginResponse[Margin] | HTTPException:
    margin = service.get_by_margin_id(request.params.id)
    if margin:
        return MarginResponse(
            code=200,
            status="Ok",
            message="Margin fetched successfully",
            result=margin,
        )
    else:
        raise HTTPException(status_code=404, detail="Margin not found!")


@margin_router.delete("/delete_margin", response_model=MarginResponse[Margin])
async def delete_margin(
    request: MarginRequest,
    service: MarginService = Depends(get_margin_service),
) -> MarginResponse[Margin] | HTTPException:
    margin = service.delete_margin(request.params)
    if margin:
        return MarginResponse(
            code=200,
            status="Ok",
            message="Margin deleted successfully",
            result=margin,
        )
    else:
        raise HTTPException(status_code=404, detail="Margin not found!")
