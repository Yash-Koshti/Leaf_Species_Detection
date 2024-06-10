import asyncio
import os
import re
import subprocess

import cv2
from firebase import Firebase
from models import Prediction, Specie
from schemas import PredictionLogSchema
from services.prediction_log_service import PredictionLogService
from services.specie_service import SpecieService
from sqlalchemy.orm import Session


class ModelService:
    def __init__(
        self,
        db: Session,
        prediction_log_service: PredictionLogService,
        specie_service: SpecieService,
        firebase: Firebase,
    ):
        self.db = db
        self.prediction_log_service = prediction_log_service
        self.specie_service = specie_service
        self.firebase = firebase

    async def predict(self, path: str):
        directory_name, img_name = path.split("/")
        self.firebase.download_from(directory_name, img_name)

        with open("source.txt", "w+") as f:
            f.write("./images/" + img_name)

        stdout, stderr = await self.run_model_command()

        data = self.read_result()

        species_based_on_result = []

        for value in data.values():
            specie = Specie(class_number=value["class_number"])
            specie = self.specie_service.get_by_class_number(specie)
            prediction = Prediction(
                class_number=value["class_number"],
                common_name=specie.common_name,
                scientific_name=specie.scientific_name,
                confidence=value["confidence"],
            )
            species_based_on_result.append(prediction)

        os.rename("predictions.jpg", img_name)

        self.firebase.upload_to("Predictions", img_name)

        os.remove("images/" + img_name)
        os.remove(img_name)

        return species_based_on_result

    async def run_model_command(self):
        command = "./darknet detector test ./ai_model/obj.data ./ai_model/yolov4.cfg ./ai_model/yolov4_final.weights -dont_show -ext_output < source.txt > result.txt"

        loop = asyncio.get_running_loop()

        def execute_command():
            process = subprocess.Popen(
                command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True
            )
            stdout, stderr = process.communicate()
            return stdout, stderr

        stdout, stderr = await loop.run_in_executor(None, execute_command)

        return stdout, stderr

    def read_result(self):
        with open("result.txt", "r") as f:
            lines = f.readlines()
            data = {}
            matches = -1
            pred_regex = r"\d+: \d+%	\(left_x:"
            for line in lines:
                if re.match(pred_regex, line):
                    matches += 1
                    pred_dict = {}
                    pred = line.split("\t")[0].split(":")
                    pred_class = int(pred[0])
                    pred_confidence = int(pred[1].split("%")[0])
                    pred_dict["class_number"] = pred_class
                    pred_dict["confidence"] = pred_confidence
                    data[matches] = pred_dict
            return data

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
