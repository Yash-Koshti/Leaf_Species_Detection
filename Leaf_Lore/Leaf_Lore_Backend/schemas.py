import uuid
from datetime import datetime

from config import Base
from models import Role
from sqlalchemy import Column, ForeignKey, Integer, String
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship


class UserSchema(Base):
    __tablename__ = "Users"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    name = Column(String, unique=True, index=True)
    email = Column(String, unique=True, index=True)
    password = Column(String)
    role = Column(String, default=Role.END_USER, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Users")
    prediction_logs = relationship("PredictionLogSchema", backref="Users")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


class SpecieSchema(Base):
    __tablename__ = "Species"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    class_number = Column(Integer, unique=True, index=True)
    common_name = Column(String, index=True)
    scientific_name = Column(String, unique=True, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Species")
    prediction_logs = relationship("PredictionLogSchema", backref="Species")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


# NOTE:
"""
In future, we can add a new table for Details of the species named "SpeciesDetails" and can add a relationship between "Species" and "SpeciesDetails" tables.
Another approach can be to store details of the species in a JSON file. You can use the class_number to map the details.
"""


class ShapeSchema(Base):
    __tablename__ = "Shapes"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    shape_name = Column(String, unique=True, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Shapes")
    prediction_logs = relationship("PredictionLogSchema", backref="Shapes")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


class ApexSchema(Base):
    __tablename__ = "Apexes"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    apex_name = Column(String, unique=True, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Apexes")
    prediction_logs = relationship("PredictionLogSchema", backref="Apexes")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


class MarginSchema(Base):
    __tablename__ = "Margins"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    margin_name = Column(String, unique=True, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Margins")
    prediction_logs = relationship("PredictionLogSchema", backref="Margins")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


class MappedImageSchema(Base):
    __tablename__ = "MappedImages"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    image_path = Column(String, unique=True, index=True)
    specie_id = Column(UUID(as_uuid=True), ForeignKey("Species.id"))
    user_id = Column(UUID(as_uuid=True), ForeignKey("Users.id"))
    shape_id = Column(UUID(as_uuid=True), ForeignKey("Shapes.id"))
    apex_id = Column(UUID(as_uuid=True), ForeignKey("Apexes.id"))
    margin_id = Column(UUID(as_uuid=True), ForeignKey("Margins.id"))
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


class PredictionLogSchema(Base):
    __tablename__ = "PredictionLogs"

    id = Column(UUID(as_uuid=True), primary_key=True, index=True, default=uuid.uuid4)
    image_path = Column(String, unique=True, index=True)
    user_id = Column(UUID(as_uuid=True), ForeignKey("Users.id"))
    specie_id = Column(UUID(as_uuid=True), ForeignKey("Species.id"))
    shape_id = Column(UUID(as_uuid=True), ForeignKey("Shapes.id"))
    apex_id = Column(UUID(as_uuid=True), ForeignKey("Apexes.id"))
    margin_id = Column(UUID(as_uuid=True), ForeignKey("Margins.id"))
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
