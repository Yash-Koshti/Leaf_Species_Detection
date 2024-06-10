from datetime import timedelta
from os import getenv

from config import SessionLocal
from dotenv import load_dotenv
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from firebase import Firebase
from jose import JWTError, jwt
from models import User
from services.apex_service import ApexService
from services.auth_service import AuthService
from services.mapped_image_service import MappedImageService
from services.margin_service import MarginService
from services.model_service import ModelService
from services.prediction_log_service import PredictionLogService
from services.shape_service import ShapeService
from services.specie_service import SpecieService
from services.user_service import UserService
from sqlalchemy.orm import Session

load_dotenv()

SECRET_KEY = getenv("SECRET_KEY")
ALGORITHM = getenv("ALGORITHM")


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/token")


def get_current_user(token: str = Depends(oauth2_scheme)) -> User:
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        name: str = payload.get("name")
        email: str = payload.get("email")
        if name is None and email is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception
    return User(name=name, email=email)


def get_user_service(db: Session = Depends(get_db)) -> UserService:
    return UserService(db)


def get_mapped_image_service(db: Session = Depends(get_db)) -> MappedImageService:
    return MappedImageService(db)


def get_specie_service(db: Session = Depends(get_db)) -> SpecieService:
    return SpecieService(db)


def get_shape_service(db: Session = Depends(get_db)) -> ShapeService:
    return ShapeService(db)


def get_apex_service(db: Session = Depends(get_db)) -> ApexService:
    return ApexService(db)


def get_margin_service(db: Session = Depends(get_db)) -> MarginService:
    return MarginService(db)


def get_prediction_log_service(db: Session = Depends(get_db)) -> PredictionLogService:
    return PredictionLogService(db)


def get_auth_service(db: Session = Depends(get_db)) -> AuthService:
    return AuthService(db)


def get_model_service(
    db: Session = Depends(get_db),
    prediction_log_service: PredictionLogService = Depends(get_prediction_log_service),
    specie_service: SpecieService = Depends(get_specie_service),
    fb: Firebase = Depends(),
) -> ModelService:
    return ModelService(db, prediction_log_service, specie_service, fb)
