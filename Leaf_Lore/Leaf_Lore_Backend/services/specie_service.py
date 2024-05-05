from repositories.specie_repository import SpecieRepository
from models import Specie
from schemas import SpecieSchema

class SpecieService:
    def __init__(self, specie_repository: SpecieRepository):
        self.specie_repository = specie_repository

    def create_specie(self, specie: Specie) -> SpecieSchema:
        specie = SpecieSchema(
            class_number=specie.class_number,
            common_name=specie.common_name,
            scientific_name=specie.scientific_name,
        )
        return self.specie_repository.create_specie(specie)
    
    def get_all_species(self) -> list[SpecieSchema]:
        return self.specie_repository.get_all_species()
    
    def get_by_class_number(self, specie: Specie) -> SpecieSchema:
        return self.specie_repository.get_by_class_number(specie.class_number)
    
    def delete_specie(self, specie: Specie) -> SpecieSchema:
        return self.specie_repository.delete_specie(specie.id)