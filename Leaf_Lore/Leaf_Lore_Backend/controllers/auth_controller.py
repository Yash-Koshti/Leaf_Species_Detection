from typing import Annotated

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordRequestForm
from models import Token, User
from services.auth_service import AuthService
from utils import get_auth_service, get_current_user

auth_router = APIRouter()


@auth_router.post("/token", response_model=Token)
async def login_for_access_token(
    form_data: Annotated[OAuth2PasswordRequestForm, Depends()],
    service: AuthService = Depends(get_auth_service),
) -> Token | HTTPException:
    user = service.authenticate_user(form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )

    access_token = service.create_access_token(
        data={"name": user.name, "email": user.email},
    )
    return Token(access_token=access_token, token_type="Bearer")


@auth_router.get("/users/me")
async def read_users_me(current_user: User = Depends(get_current_user)):
    return "Already logged in!"
