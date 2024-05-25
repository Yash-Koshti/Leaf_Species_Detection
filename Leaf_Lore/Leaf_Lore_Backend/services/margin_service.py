from fastapi.logger import logger
from models import Margin, MarginRequest, MarginResponse
from schemas import MarginSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class MarginService:
    def __init__(self, db: Session):
        self.db = db

    def create_margin(self, margin: Margin) -> MarginSchema:
        margin = MarginSchema(margin_name=margin.margin_name)
        try:
            self.db.add(margin)
            self.db.commit()
            self.db.refresh(margin)
            return margin
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating margin: {e}")
            return None

    def get_all_margins(self) -> list[MarginSchema]:
        return self.db.query(MarginSchema).all()

    def get_by_margin_id(self, margin_id: int) -> MarginSchema:
        return self.db.query(MarginSchema).filter(MarginSchema.id == margin_id).first()

    def delete_margin(self, margin_id: int) -> MarginSchema:
        margin = (
            self.db.query(MarginSchema).filter(MarginSchema.id == margin_id).first()
        )
        if margin:
            self.db.delete(margin)
            self.db.commit()
        return margin
