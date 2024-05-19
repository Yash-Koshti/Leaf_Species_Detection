import os

import sqlalchemy.dialects.postgresql
from dotenv import load_dotenv
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# Load the environment variables
load_dotenv()

if os.getenv("DATABASE_URL") is None:
    raise Exception("DATABASE_URL environment variable is not set")

# database_host = os.getenv("DATABASE_HOST", "db")

# Example URL: postgresql://user:password@host:port/db_name
# DATABASE_URL = f"postgresql://{os.getenv("POSTGRES_USER")}:{os.getenv("POSTGRES_PASSWORD")}@{database_host}:5432/{os.getenv("POSTGRES_DB")}"

# Create the database engine
engine = create_engine(os.getenv("DATABASE_URL"))
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()
