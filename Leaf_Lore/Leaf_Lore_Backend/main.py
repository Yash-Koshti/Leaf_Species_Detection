import asyncio
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
    shape_controller,
    specie_controller,
    user_controller,
)
from fastapi import FastAPI

schemas.Base.metadata.create_all(bind=engine)

app = FastAPI()


def run_migrations():
    alembic_cfg = Config("alembic.ini")
    command.upgrade(alembic_cfg, "head")


@asynccontextmanager
async def lifespan(app: FastAPI):
    loop = asyncio.get_event_loop()
    await loop.run_in_executor(None, run_migrations)
    yield


@app.get("/")
async def read_root():
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
