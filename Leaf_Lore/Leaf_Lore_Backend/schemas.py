from datetime import datetime

from config import Base
from models import Role
from sqlalchemy import Column, ForeignKey, Integer, String
from sqlalchemy.orm import relationship


class UserSchema(Base):
    __tablename__ = "Users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)
    email = Column(String, unique=True, index=True)
    password = Column(String)
    role = Column(String, default=Role.END_USER, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Users")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


class SpecieSchema(Base):
    __tablename__ = "Species"

    id = Column(Integer, primary_key=True, index=True)
    class_number = Column(Integer, unique=True, index=True)
    common_name = Column(String, index=True)
    scientific_name = Column(String, unique=True, index=True)
    mapped_images = relationship("MappedImageSchema", backref="Species")
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))


# NOTE:
"""
In future, we can add a new table for Details of the species named "SpeciesDetails" and can add a relationship between "Species" and "SpeciesDetails" tables.
Another approach can be to store details of the species in a JSON file. You can use the class_number to map the details.
"""


class MappedImageSchema(Base):
    __tablename__ = "MappedImages"

    id = Column(Integer, primary_key=True, index=True)
    image_name = Column(String, unique=True, index=True)
    specie_id = Column(Integer, ForeignKey("Species.id"))
    user_id = Column(Integer, ForeignKey("Users.id"))
    created_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    updated_at = Column(String, default=datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
