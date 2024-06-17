import asyncio
import os
import subprocess
from contextlib import asynccontextmanager

import schemas
from alembic import command
from alembic.config import Config
from config import engine
from controllers import (
    apex_controller,
    auth_controller,
    mapped_image_controller,
    margin_controller,
    model_controller,
    shape_controller,
    specie_controller,
    user_controller,
)
from fastapi import FastAPI
from firebase import Firebase

schemas.Base.metadata.create_all(bind=engine)

app = FastAPI()


def run_migrations():
    alembic_cfg = Config("alembic.ini")
    command.upgrade(alembic_cfg, "head")


def download_model_weights():
    fb = Firebase()
    fb.download_from("Yolov4_weights", "yolov4_v1.weights", "ai_model/model_v1")


def create_dir():
    if not os.path.exists("images"):
        os.makedirs("images")


async def run_command(command: str):
    loop = asyncio.get_running_loop()

    def execute_command():
        process = subprocess.Popen(
            command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True
        )
        stdout, stderr = process.communicate()
        return stdout, stderr

    stdout, stderr = await loop.run_in_executor(None, execute_command)
    print("Stdout:", convert_byte_to_string(stdout))
    print("Stderr:", convert_byte_to_string(stderr))


def convert_byte_to_string(byte_string):
    return "\n".join([line.decode("utf-8") for line in byte_string.split(b"\n")])


@asynccontextmanager
async def lifespan(app: FastAPI):
    loop = asyncio.get_event_loop()
    await loop.run_in_executor(None, run_migrations)
    yield


download_model_weights()
create_dir()


@app.get("/")
async def read_root():
    # await run_command("ls -l /usr/lib/x86_64-linux-gnu | grep opencv")
    await run_command("pwd")
    print("----------")
    await run_command("ls")
    print("----------")

    return {
        "Hello": "This is Leaf Lore server! I'm ready to detect leaves!",
        "routes": {
            "user": [
                "POST /user/register",
                "GET /user/login",
                "POST /user/update_user",
                "DELETE /user/delete_user",
            ],
            "mapped_image": [
                "GET /mapped_image/all_mapped_images",
                "POST /mapped_image/create_mapped_image",
                "GET /mapped_image/all_image_names",
                "DELETE /mapped_image/delete_mapped_image",
            ],
            "specie": [
                "GET /specie/all_species",
                "POST /specie/create_specie",
                "GET /specie/get_by_class_number",
                "DELETE /specie/delete_specie",
            ],
            "shape": [
                "GET /shape/all_shapes",
                "POST /shape/create_shape",
                "GET /shape/get_by_shape_id",
                "DELETE /shape/delete_shape",
            ],
            "apex": [
                "GET /apex/all_apexes",
                "POST /apex/create_apex",
                "GET /apex/get_by_apex_id",
                "DELETE /apex/delete_apex",
            ],
            "margin": [
                "GET /margin/all_margins",
                "POST /margin/create_margin",
                "GET /margin/get_by_margin_id",
                "DELETE /margin/delete_margin",
            ],
        },
    }


app.include_router(auth_controller.auth_router, prefix="/auth", tags=["auth"])
app.include_router(user_controller.user_router, prefix="/user", tags=["User"])
app.include_router(
    mapped_image_controller.mapped_image_router,
    prefix="/mapped_image",
    tags=["Mapped Image"],
)
app.include_router(specie_controller.specie_router, prefix="/specie", tags=["Specie"])
app.include_router(shape_controller.shape_router, prefix="/shape", tags=["Shape"])
app.include_router(apex_controller.apex_router, prefix="/apex", tags=["Apex"])
app.include_router(margin_controller.margin_router, prefix="/margin", tags=["Margin"])
app.include_router(model_controller.model_router, prefix="/model", tags=["Model"])
