from config import SessionLocal
from sqlalchemy.orm import Session
from fastapi import Depends

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