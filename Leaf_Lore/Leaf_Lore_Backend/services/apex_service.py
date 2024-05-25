from fastapi.logger import logger
from models import Apex
from schemas import ApexSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class ApexService:
    def __init__(self, db: Session):
        self.db = db

    def create_apex(self, apex: Apex) -> ApexSchema:
        apex = ApexSchema(apex_name=apex.apex_name)
        try:
            self.db.add(apex)
            self.db.commit()
            self.db.refresh(apex)
            return apex
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating apex: {e}")
            return None

    def get_all_apexes(self) -> list[Apex]:
        return self.db.query(ApexSchema).all()

    def get_by_apex_id(self, apex_id: int) -> ApexSchema:
        return self.db.query(ApexSchema).filter(ApexSchema.id == apex_id).first()

    def delete_apex(self, apex_id: int) -> ApexSchema:
        apex = self.db.query(ApexSchema).filter(ApexSchema.id == apex_id).first()
        if apex:
            self.db.delete(apex)
            self.db.commit()
        return apex
