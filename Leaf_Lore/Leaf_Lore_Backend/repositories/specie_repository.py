from sqlalchemy.orm import Session
from schemas import SpecieSchema
from sqlalchemy.exc import SQLAlchemyError
from fastapi.logger import logger

class SpecieRepository:
    def __init__(self, db: Session):
        self.db = db

    def create_specie(self, specie: SpecieSchema) -> SpecieSchema | None:
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
    
    def get_by_class_number(self, class_number: int) -> SpecieSchema:
        return self.db.query(SpecieSchema).filter(SpecieSchema.class_number == class_number).first()
    
    def delete_specie(self, specie_id: int) -> SpecieSchema:
        specie = (
            self.db.query(SpecieSchema)
            .filter(SpecieSchema.id == specie_id)
            .first()
        )
        if specie:
            self.db.delete(specie)
            self.db.commit()
        return specie