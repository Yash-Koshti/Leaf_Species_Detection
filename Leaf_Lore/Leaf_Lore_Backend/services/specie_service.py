from fastapi.logger import logger
from models import Specie
from schemas import SpecieSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class SpecieService:
    def __init__(self, db: Session):
        self.db = db

    def create_specie(self, specie: Specie) -> SpecieSchema:
        specie = SpecieSchema(
            class_number=specie.class_number,
            common_name=specie.common_name,
            scientific_name=specie.scientific_name,
        )
        try:
            self.db.add(specie)
            self.db.commit()
            self.db.refresh(specie)
            return specie
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating specie: {e}")
            return None

    def get_all_species(self) -> list[SpecieSchema]:
        return self.db.query(SpecieSchema).all()

    def get_by_class_number(self, specie: Specie) -> SpecieSchema:
        return (
            self.db.query(SpecieSchema)
            .filter(SpecieSchema.class_number == specie.class_number)
            .first()
        )

    def delete_specie(self, specie: Specie) -> SpecieSchema:
        specie = (
            self.db.query(SpecieSchema).filter(SpecieSchema.id == specie.id).first()
        )
        if specie:
            self.db.delete(specie)
            self.db.commit()
        return specie
