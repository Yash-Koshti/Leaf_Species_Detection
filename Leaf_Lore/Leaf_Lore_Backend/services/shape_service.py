from fastapi.logger import logger
from models import Shape
from schemas import ShapeSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class ShapeService:
    def __init__(self, db: Session):
        self.db = db

    def create_shape(self, shape: Shape) -> ShapeSchema:
        shape = ShapeSchema(shape_name=shape.shape_name)
        try:
            self.db.add(shape)
            self.db.commit()
            self.db.refresh(shape)
            return shape
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating shape: {e}")
            return None

    def get_all_shapes(self) -> list[Shape]:
        return self.db.query(ShapeSchema).all()

    def get_by_shape_id(self, shape_id: int) -> ShapeSchema:
        return self.db.query(ShapeSchema).filter(ShapeSchema.id == shape_id).first()

    def delete_shape(self, shape_id: int) -> ShapeSchema:
        shape = self.db.query(ShapeSchema).filter(ShapeSchema.id == shape_id).first()
        if shape:
            self.db.delete(shape)
            self.db.commit()
        return shape
