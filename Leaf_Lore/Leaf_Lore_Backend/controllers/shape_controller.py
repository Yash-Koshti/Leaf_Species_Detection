from fastapi import APIRouter, Depends, HTTPException, status
from models import Shape, ShapeRequest, ShapeResponse, User
from services.shape_service import ShapeService
from utils import get_current_user, get_shape_service

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
            code=status.HTTP_200_OK,
            status="Ok",
            message="Shapes fetched successfully",
            result=shapes,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No shapes found!"
        )


@shape_router.post("/create_shape", response_model=ShapeResponse[Shape])
async def create_shape(
    request: ShapeRequest,
    service: ShapeService = Depends(get_shape_service),
    current_user: User = Depends(get_current_user),
) -> ShapeResponse[Shape] | HTTPException:
    shape = service.create_shape(request.params)
    if shape:
        return ShapeResponse(
            code=status.HTTP_201_CREATED,
            status="Created",
            message="Shape created successfully",
            result=shape,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Internal Server Error!",
        )


@shape_router.get("/get_by_shape_id", response_model=ShapeResponse[Shape])
async def get_by_shape_id(
    request: ShapeRequest,
    service: ShapeService = Depends(get_shape_service),
    current_user: User = Depends(get_current_user),
) -> ShapeResponse[Shape] | HTTPException:
    shape = service.get_by_shape_id(request.params.id)
    if shape:
        return ShapeResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Shape fetched successfully",
            result=shape,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Shape not found!"
        )


@shape_router.delete("/delete_shape", response_model=ShapeResponse[Shape])
async def delete_shape(
    request: ShapeRequest,
    service: ShapeService = Depends(get_shape_service),
    current_user: User = Depends(get_current_user),
) -> ShapeResponse[Shape] | HTTPException:
    shape = service.delete_shape(request.params)
    if shape:
        return ShapeResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Shape deleted successfully",
            result=shape,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Shape not found!"
        )
