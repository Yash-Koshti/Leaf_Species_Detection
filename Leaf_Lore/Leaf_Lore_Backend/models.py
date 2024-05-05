from typing import Optional, Generic, TypeVar, List
from pydantic import BaseModel, Field
from enum import Enum

T = TypeVar("T")


class Role(str, Enum):
    ADMIN = "admin"
    END_USER = "end_user"
    RESEARCHER = "researcher"


class User(BaseModel):
    id: Optional[int] = None
    name: Optional[str] = None
    email: Optional[str] = None
    password: Optional[str] = None
    role: Optional[Role] = None

    class Config:
        from_attributes = True


class UserRequest(BaseModel):
    params: User = Field(...)


class UserResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]


class MappedImage(BaseModel):
    id: Optional[int] = None
    image_name: Optional[str] = None
    specie_id: Optional[int] = None
    user_id: Optional[int] = None

    class Config:
        from_attributes = True


class MappedImageRequest(BaseModel):
    params: MappedImage = Field(...)


class MappedImageResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]


class Specie(BaseModel):
    id: Optional[int] = None
    class_number: Optional[int] = None
    common_name: Optional[str] = None
    scientific_name: Optional[str] = None

    class Config:
        from_attributes = True


class SpecieRequest(BaseModel):
    params: Specie = Field(...)


class SpecieResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]
