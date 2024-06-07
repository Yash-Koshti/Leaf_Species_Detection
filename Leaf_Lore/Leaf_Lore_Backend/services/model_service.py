from os import path, walk

from firebase import Firebase
from schemas import PredictionLogSchema
from services.prediction_log_service import PredictionLogService
from sqlalchemy.orm import Session


class ModelService:
    def __init__(
        self,
        db: Session,
        # model,
        prediction_log_service: PredictionLogService,
        firebase: Firebase,
    ):
        self.db = db
        # self.model = model
        self.prediction_log_service = prediction_log_service
        self.firebase = firebase

    def predict(self, path: str):
        directory_name, img_name = path.split("/")
        self.firebase.download_from(directory_name, img_name)

        file_paths = self.list_files_in_directory(self.firebase.local_images_path)
        self.write_file_paths_to_txt(file_paths, "train.txt")

        return {}

    def log_prediction(self, data):
        self.prediction_log_service.create_prediction_log(
            PredictionLogSchema(
                image_name=data["image_name"],
                user_id=data["user_id"],
                specie_id=data["specie_id"],
                shape_id=data["shape_id"],
                apex_id=data["apex_id"],
                margin_id=data["margin_id"],
            )
        )

    def list_files_in_directory(self, directory):
        file_paths = []
        for root, _, files in walk(directory):
            for file in files:
                file_paths.append(path.join(root, file))
        return file_paths

    def write_file_paths_to_txt(self, file_paths, output_file):
        with open(output_file, "w+") as f:
            for path in file_paths:
                f.write(path + "\n")
