import schemas
from config import engine
from controllers import mapped_image_controller, specie_controller, user_controller
from fastapi import FastAPI

schemas.Base.metadata.create_all(bind=engine)

app = FastAPI()


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
        },
    }


app.include_router(user_controller.user_router, prefix="/user", tags=["User"])
app.include_router(
    mapped_image_controller.mapped_image_router,
    prefix="/mapped_image",
    tags=["Mapped Image"],
)
app.include_router(specie_controller.specie_router, prefix="/specie", tags=["Specie"])
