from fastapi import APIRouter, Depends, HTTPException, status
from models import Apex, ApexRequest, ApexResponse, User
from services.apex_service import ApexService
from utils import get_apex_service, get_current_user

apex_router = APIRouter()


@apex_router.get("/")
async def read_root():
    return "Available endpoints: GET /all_apexes  POST /create_apex  GET /get_by_apex_id  DELETE /delete_apex"


@apex_router.get("/all_apexes", response_model=ApexResponse[list[Apex]])
async def get_all_apexes(
    service: ApexService = Depends(get_apex_service),
) -> ApexResponse[list[Apex]] | HTTPException:
    apexes = service.get_all_apexes()
    if apexes:
        return ApexResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Apexes fetched successfully",
            result=apexes,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="No apexes found!"
        )


@apex_router.post("/create_apex", response_model=ApexResponse[Apex])
async def create_apex(
    request: ApexRequest,
    service: ApexService = Depends(get_apex_service),
    current_user: User = Depends(get_current_user),
) -> ApexResponse[Apex] | HTTPException:
    apex = service.create_apex(request.params)
    if apex:
        return ApexResponse(
            code=status.HTTP_201_CREATED,
            status="Created",
            message="Apex created successfully",
            result=apex,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Internal Server Error!",
        )


@apex_router.get("/get_by_apex_id", response_model=ApexResponse[Apex])
async def get_by_apex_id(
    request: ApexRequest,
    service: ApexService = Depends(get_apex_service),
    current_user: User = Depends(get_current_user),
) -> ApexResponse[Apex] | HTTPException:
    apex = service.get_by_apex_id(request.params.id)
    if apex:
        return ApexResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Apex fetched successfully",
            result=apex,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Apex not found!"
        )


@apex_router.delete("/delete_apex", response_model=ApexResponse[Apex])
async def delete_apex(
    request: ApexRequest,
    service: ApexService = Depends(get_apex_service),
    current_user: User = Depends(get_current_user),
) -> ApexResponse[Apex] | HTTPException:
    apex = service.delete_apex(request.params)
    if apex:
        return ApexResponse(
            code=status.HTTP_200_OK,
            status="Ok",
            message="Apex deleted successfully",
            result=apex,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Apex not found!"
        )
