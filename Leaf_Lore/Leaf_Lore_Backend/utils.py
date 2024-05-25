from config import SessionLocal
from sqlalchemy.orm import Session
from fastapi import Depends

from repositories.user_repository import UserRepository
from repositories.mapped_image_repository import MappedImageRepository
from repositories.specie_repository import SpecieRepository
from services.user_service import UserService
from services.mapped_image_service import MappedImageService
from services.specie_service import SpecieService
from services.shape_service import ShapeService
from services.apex_service import ApexService
from services.margin_service import MarginService
from services.prediction_log_service import PredictionLogService

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

def get_user_repository(db: Session = Depends(get_db)) -> UserRepository:
    return UserRepository(db)

def get_user_service(user_repository: UserRepository = Depends(get_user_repository)) -> UserService:
    return UserService(user_repository)

def get_mapped_image_repository(db: Session = Depends(get_db)) -> MappedImageRepository:
    return MappedImageRepository(db)

def get_mapped_image_service(mapped_image_repository: MappedImageRepository = Depends(get_mapped_image_repository)) -> MappedImageService:
    return MappedImageService(mapped_image_repository)

def get_specie_repository(db: Session = Depends(get_db)) -> SpecieRepository:
    return SpecieRepository(db)

def get_specie_service(specie_repository: SpecieRepository = Depends(get_specie_repository)) -> SpecieService:
    return SpecieService(specie_repository)

def get_shape_service(db: Session = Depends(get_db)) -> ShapeService:
    return ShapeService(db)

def get_apex_service(db: Session = Depends(get_db)) -> ApexService:
    return ApexService(db)

def get_margin_service(db: Session = Depends(get_db)) -> MarginService:
    return MarginService(db)

def get_prediction_log_service(db: Session = Depends(get_db)) -> PredictionLogService:
    return PredictionLogService(db)