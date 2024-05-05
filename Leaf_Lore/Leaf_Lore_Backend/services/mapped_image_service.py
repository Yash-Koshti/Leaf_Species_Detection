from repositories.mapped_image_repository import MappedImageRepository
from models import MappedImage
from schemas import MappedImageSchema


class MappedImageService:
    def __init__(self, mapped_image_repository: MappedImageRepository):
        self.mapped_image_repository = mapped_image_repository

    def get_all_mapped_images(self) -> list[MappedImageSchema]:
        return self.mapped_image_repository.get_all_mapped_images()

    def create_mapped_image(self, mapped_image: MappedImage) -> MappedImageSchema:
        mapped_image = MappedImageSchema(
            image_name=mapped_image.image_name,
            specie_id=mapped_image.specie_id,
            user_id=mapped_image.user_id,
        )
        return self.mapped_image_repository.create_mapped_image(mapped_image)

    def get_all_image_names(self) -> list[str] | None:
        image_name_rows = self.mapped_image_repository.get_all_image_names()
        return (
            [image_name_row[0] for image_name_row in image_name_rows]
            if image_name_rows
            else None
        )

    def delete_mapped_image(self, mapped_image: MappedImage) -> MappedImageSchema:
        return self.mapped_image_repository.delete_mapped_image(mapped_image.id)
