from fastapi.logger import logger
from models import MappedImage
from schemas import MappedImageSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class MappedImageService:
    def __init__(self, db: Session):
        self.db = db

    def get_all_mapped_images(self) -> list[MappedImageSchema]:
        return self.db.query(MappedImageSchema).all()

    def create_mapped_image(self, mapped_image: MappedImage) -> MappedImageSchema:
        mapped_image = MappedImageSchema(
            image_name=mapped_image.image_path,
            specie_id=mapped_image.specie_id,
            user_id=mapped_image.user_id,
            shape_id=mapped_image.shape_id,
            apex_id=mapped_image.apex_id,
            margin_id=mapped_image.margin_id,
        )
        try:
            self.db.add(mapped_image)
            self.db.commit()
            self.db.refresh(mapped_image)
            return mapped_image
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating mapped image: {e}")
            return None

    def get_all_image_names(self) -> list[str] | None:
        image_path_rows = self.db.query(MappedImageSchema.image_path).all()
        return (
            [image_path_row[0] for image_path_row in image_path_rows]
            if image_path_rows
            else None
        )

    def delete_mapped_image(self, mapped_image: MappedImage) -> MappedImageSchema:
        mapped_image = (
            self.db.query(MappedImageSchema)
            .filter(MappedImageSchema.id == mapped_image.id)
            .first()
        )
        if mapped_image:
            self.db.delete(mapped_image)
            self.db.commit()
        return mapped_image
