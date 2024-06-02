from fastapi import APIRouter, Depends, HTTPException
from models import User, UserRequest, UserResponse
from services.user_service import UserService
from utils import get_user_service, get_current_user

user_router = APIRouter()


@user_router.get("/")
async def read_root():
    return "Available endpoints: POST /user/create_user  GET /user/login  POST /user/update_user  DELETE /user/delete_user"


@user_router.post("/register", response_model=UserResponse[User])
async def register(
    request: UserRequest, service: UserService = Depends(get_user_service)
) -> UserResponse[User] | HTTPException:
    user = service.register(request.params)
    if user:
        user = User(
            name=user.name,
            email=user.email,
            role=user.role,
            created_at=user.created_at,
            updated_at=user.updated_at,
        )
        return UserResponse(
            code=200, status="Ok", message="User created successfully", result=user
        )
    else:
        raise HTTPException(status_code=500, detail="Internal Server Error!")


@user_router.get("/login")
async def login(
    current_user: User = Depends(get_current_user), service: UserService = Depends(get_user_service)
) -> UserResponse[User]:
    user = service.login(current_user)
    if user:
        user = User(
            id=user.id,
            name=user.name,
            email=user.email,
            role=user.role,
            created_at=user.created_at,
            updated_at=user.updated_at,
        )
        return UserResponse(
            code=200, status="Ok", message="Login successful!", result=user
        )
    else:
        raise HTTPException(status_code=404, detail="User not found!")


@user_router.post("/update_user", response_model=UserResponse[User])
async def update_user(
    request: UserRequest, service: UserService = Depends(get_user_service)
) -> UserResponse[User]:
    user = service.update_user(request.params)
    if user:
        user = User(id=user.id, name=user.name, email=user.email, role=user.role)
        return UserResponse(
            code=200, status="Ok", message="User updated successfully", result=user
        )
    else:
        raise HTTPException(status_code=404, detail="User not found!")


@user_router.delete("/delete_user", response_model=UserResponse[User])
async def delete_user(
    request: UserRequest, service: UserService = Depends(get_user_service)
) -> UserResponse[User]:
    user = service.delete_user(request.params)
    if user:
        user = User(id=user.id, name=user.name, email=user.email, role=user.role)
        return UserResponse(
            code=200, status="Ok", message="User deleted successfully", result=user
        )
    else:
        raise HTTPException(status_code=404, detail="User not found!")
