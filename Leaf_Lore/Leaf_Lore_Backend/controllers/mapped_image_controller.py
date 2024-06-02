from fastapi import APIRouter, Depends, HTTPException, status
from models import MappedImage, MappedImageRequest, MappedImageResponse, User
from services.mapped_image_service import MappedImageService
from utils import get_current_user, get_mapped_image_service

mapped_image_router = APIRouter()


@mapped_image_router.get("/")
async def read_root():
    return "Available endpoints: GET /all_mapped_images  POST /create_mapped_image  GET /all_image_names  DELETE /delete_mapped_image"


@mapped_image_router.get(
    "/all_mapped_images", response_model=MappedImageResponse[list[MappedImage]]
)
async def get_all_mapped_images(
    service: MappedImageService = Depends(get_mapped_image_service),
    current_user: User = Depends(get_current_user),
) -> MappedImageResponse[list[MappedImage]] | HTTPException:
    mapped_images = service.get_all_mapped_images()
    if mapped_images:
        return MappedImageResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Mapped images fetched successfully",
            result=mapped_images,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No mapped images found!"
        )


@mapped_image_router.post(
    "/create_mapped_image", response_model=MappedImageResponse[MappedImage]
)
async def create_mapped_image(
    request: MappedImageRequest,
    service: MappedImageService = Depends(get_mapped_image_service),
    current_user: User = Depends(get_current_user),
) -> MappedImageResponse[MappedImage] | HTTPException:
    mapped_image = service.create_mapped_image(request.params)
    if mapped_image:
        return MappedImageResponse(
            code=status.HTTP_201_CREATED,
            status="Created",
            message="Mapped image created successfully",
            result=mapped_image,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Internal Server Error!",
        )


@mapped_image_router.get(
    "/all_image_names", response_model=MappedImageResponse[list[str]]
)
async def get_all_image_names(
    service: MappedImageService = Depends(get_mapped_image_service),
    current_user: User = Depends(get_current_user),
) -> MappedImageResponse[list[str]] | HTTPException:
    image_name_list = service.get_all_image_names()
    if image_name_list:
        return MappedImageResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Image names fetched successfully",
            result=image_name_list,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No mapped image names found!"
        )


@mapped_image_router.delete(
    "/delete_mapped_image", response_model=MappedImageResponse[MappedImage]
)
async def delete_mapped_image(
    request: MappedImageRequest,
    service: MappedImageService = Depends(get_mapped_image_service),
    current_user: User = Depends(get_current_user),
):
    mapped_image = service.delete_mapped_image(request.params)
    if mapped_image:
        return MappedImageResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Mapped image deleted successfully",
            result=mapped_image,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Mapped image not found!"
        )
