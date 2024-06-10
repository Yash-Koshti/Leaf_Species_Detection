import asyncio
import os
import subprocess
import cv2
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

    async def predict(self, path: str):
        directory_name, img_name = path.split("/")
        self.firebase.download_from(directory_name, img_name)

        stdout, stderr = await self.run_command(
            os.path.join(self.firebase.local_images_path, img_name)
        )

        return {
            "stdout": str(stdout.decode()),
            "stderr": str(stderr.decode()),
            "message": "Prediction successful.",
        }

    async def run_command(self, image_path: str):
        command = f"./darknet-master detector test ai_model\obj.data ai_model\yolov4.cfg ai_model\yolov4_final.weights -ext_output {image_path}"

        loop = asyncio.get_running_loop()

        def execute_command():
            process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
            stdout, stderr = process.communicate()
            return stdout, stderr

        stdout, stderr = await loop.run_in_executor(None, execute_command)

        return stdout, stderr

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
        for root, _, files in os.walk(directory):
            for file in files:
                file_paths.append(os.path.join(root, file))
        return file_paths

    def write_file_paths_to_txt(self, file_paths, output_file):
        with open(output_file, "w+") as f:
            for path in file_paths:
                f.write(path + "\n")
