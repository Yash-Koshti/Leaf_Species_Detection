from uuid import UUID

from fastapi.logger import logger
from models import PredictionLog
from schemas import PredictionLogSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class PredictionLogService:
    def __init__(self, db: Session):
        self.db = db

    def create_prediction_log(
        self, prediction_log: PredictionLog
    ) -> PredictionLogSchema:
        prediction_log = PredictionLogSchema(
            image_name=prediction_log.image_path,
            user_id=prediction_log.user_id,
            specie_id=prediction_log.specie_id,
            shape_id=prediction_log.shape_id,
            apex_id=prediction_log.apex_id,
            margin_id=prediction_log.margin_id,
        )
        try:
            self.db.add(prediction_log)
            self.db.commit()
            self.db.refresh(prediction_log)
            return prediction_log
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating prediction log: {e}")
            return None

    def get_all_prediction_logs(self) -> list[PredictionLogSchema]:
        return self.db.query(PredictionLogSchema).all()

    def get_by_prediction_log_id(self, prediction_log_id: UUID) -> PredictionLogSchema:
        return (
            self.db.query(PredictionLogSchema)
            .filter(PredictionLogSchema.id == prediction_log_id)
            .first()
        )

    def delete_prediction_log(
        self, prediction_log: PredictionLog
    ) -> PredictionLogSchema:
        prediction_log = (
            self.db.query(PredictionLogSchema)
            .filter(PredictionLogSchema.id == prediction_log.id)
            .first()
        )
        if prediction_log:
            self.db.delete(prediction_log)
            self.db.commit()
        return prediction_log
