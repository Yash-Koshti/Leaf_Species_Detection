from sqlalchemy.orm import Session
from schemas import MappedImageSchema
from models import MappedImage
from sqlalchemy.exc import SQLAlchemyError
from fastapi.logger import logger


class MappedImageRepository:
    def __init__(self, db: Session):
        self.db = db

    def get_all_mapped_images(self) -> list[MappedImageSchema]:
        return self.db.query(MappedImageSchema).all()

    def create_mapped_image(
        self, mapped_image: MappedImageSchema
    ) -> MappedImageSchema | None:
        try:
            self.db.add(mapped_image)
            self.db.commit()
            self.db.refresh(mapped_image)
            return mapped_image
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating mapped image: {e}")
            return None

    def get_all_image_names(self) -> list[MappedImageSchema]:
        return self.db.query(MappedImageSchema.image_name).all()

    def delete_mapped_image(self, mapped_image_id: int) -> MappedImageSchema:
        mapped_image = (
            self.db.query(MappedImageSchema)
            .filter(MappedImageSchema.id == mapped_image_id)
            .first()
        )
        if mapped_image:
            self.db.delete(mapped_image)
            self.db.commit()
        return mapped_image
