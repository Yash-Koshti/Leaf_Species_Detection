from enum import Enum
from typing import Generic, List, Optional, TypeVar

from pydantic import BaseModel, Field

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
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

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
    shape_id: Optional[int] = None
    apex_id: Optional[int] = None
    margin_id: Optional[int] = None
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

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
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

    class Config:
        from_attributes = True


class SpecieRequest(BaseModel):
    params: Specie = Field(...)


class SpecieResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]


class Shape(BaseModel):
    id: Optional[int] = None
    shape_name: Optional[str] = None
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

    class Config:
        from_attributes = True


class ShapeRequest(BaseModel):
    params: Shape = Field(...)


class ShapeResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]


class Apex(BaseModel):
    id: Optional[int] = None
    apex_name: Optional[str] = None
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

    class Config:
        from_attributes = True


class ApexRequest(BaseModel):
    params: Apex = Field(...)


class ApexResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]


class Margin(BaseModel):
    id: Optional[int] = None
    margin_name: Optional[str] = None
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

    class Config:
        from_attributes = True


class MarginRequest(BaseModel):
    params: Margin = Field(...)


class MarginResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]


class PredictionLog(BaseModel):
    id: Optional[int] = None
    image_name: Optional[str] = None
    user_id: Optional[int] = None
    specie_id: Optional[int] = None
    shape_id: Optional[int] = None
    apex_id: Optional[int] = None
    margin_id: Optional[int] = None
    created_at: Optional[str] = None
    updated_at: Optional[str] = None

    class Config:
        from_attributes = True


class PredictionLogRequest(BaseModel):
    params: PredictionLog = Field(...)


class PredictionLogResponse(BaseModel, Generic[T]):
    code: int
    status: str
    message: str
    result: Optional[T]
