from fastapi import APIRouter, Depends, HTTPException
from models import Shape, ShapeRequest, ShapeResponse
from services.shape_service import ShapeService
from utils import get_shape_service

shape_router = APIRouter()


@shape_router.get("/")
async def read_root():
    return "Available endpoints: GET /all_shapes  POST /create_shape  GET /get_by_shape_id  DELETE /delete_shape"


@shape_router.get("/all_shapes", response_model=ShapeResponse[list[Shape]])
async def get_all_shapes(
    service: ShapeService = Depends(get_shape_service),
) -> ShapeResponse[list[Shape]] | HTTPException:
    shapes = service.get_all_shapes()
    if shapes:
        return ShapeResponse(
            code=200,
            status="Ok",
            message="Shapes fetched successfully",
            result=shapes,
        )
    else:
        raise HTTPException(status_code=404, detail="No shapes found!")
    

@shape_router.post("/create_shape", response_model=ShapeResponse[Shape])
async def create_shape(
    request: ShapeRequest,
    service: ShapeService = Depends(get_shape_service),
) -> ShapeResponse[Shape] | HTTPException:
    shape = service.create_shape(request.params)
    if shape:
        return ShapeResponse(
            code=200,
            status="Ok",
            message="Shape created successfully",
            result=shape,
        )
    else:
        raise HTTPException(status_code=500, detail="Internal Server Error!")
    

@shape_router.get("/get_by_shape_id", response_model=ShapeResponse[Shape])
async def get_by_shape_id(
    request: ShapeRequest,
    service: ShapeService = Depends(get_shape_service),
) -> ShapeResponse[Shape] | HTTPException:
    shape = service.get_by_shape_id(request.params.id)
    if shape:
        return ShapeResponse(
            code=200,
            status="Ok",
            message="Shape fetched successfully",
            result=shape,
        )
    else:
        raise HTTPException(status_code=404, detail="Shape not found!")
    

@shape_router.delete("/delete_shape", response_model=ShapeResponse[Shape])
async def delete_shape(
    request: ShapeRequest,
    service: ShapeService = Depends(get_shape_service),
) -> ShapeResponse[Shape] | HTTPException:
    shape = service.delete_shape(request.params.id)
    if shape:
        return ShapeResponse(
            code=200,
            status="Ok",
            message="Shape deleted successfully",
            result=shape,
        )
    else:
        raise HTTPException(status_code=404, detail="Shape not found!")