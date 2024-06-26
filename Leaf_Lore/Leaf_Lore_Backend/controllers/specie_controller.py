from fastapi import APIRouter, Depends, HTTPException, status
from models import Specie, SpecieRequest, SpecieResponse, User
from services.specie_service import SpecieService
from utils import get_current_user, get_specie_service

specie_router = APIRouter()


@specie_router.get("/")
async def read_root():
    return "Available endpoints: GET /all_species  POST /create_specie  GET /get_by_class_number  DELETE /delete_specie"


@specie_router.get("/all_species", response_model=SpecieResponse[list[Specie]])
async def get_all_species(
    service: SpecieService = Depends(get_specie_service),
) -> SpecieResponse[list[Specie]] | HTTPException:
    species = service.get_all_species()
    if species:
        return SpecieResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Species fetched successfully",
            result=species,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No species found!"
        )


@specie_router.post("/create_specie", response_model=SpecieResponse[Specie])
async def create_specie(
    request: SpecieRequest,
    service: SpecieService = Depends(get_specie_service),
    current_user: User = Depends(get_current_user),
) -> SpecieResponse[Specie] | HTTPException:
    specie = service.create_specie(request.params)
    if specie:
        return SpecieResponse(
            code=status.HTTP_201_CREATED,
            status="Created",
            message="Specie created successfully",
            result=specie,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Internal Server Error!",
        )


@specie_router.get("/get_by_class_number", response_model=SpecieResponse[Specie])
async def get_by_class_number(
    request: SpecieRequest,
    service: SpecieService = Depends(get_specie_service),
) -> SpecieResponse[Specie] | HTTPException:
    specie = service.get_by_class_number(request.params)
    if specie:
        return SpecieResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Specie fetched successfully",
            result=specie,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Specie not found!"
        )


@specie_router.delete("/delete_specie", response_model=SpecieResponse[Specie])
async def delete_specie(
    request: SpecieRequest,
    service: SpecieService = Depends(get_specie_service),
    current_user: User = Depends(get_current_user),
) -> SpecieResponse[Specie] | HTTPException:
    specie = service.delete_specie(request.params)
    if specie:
        return SpecieResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Specie deleted successfully",
            result=specie,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Specie not found!"
        )
