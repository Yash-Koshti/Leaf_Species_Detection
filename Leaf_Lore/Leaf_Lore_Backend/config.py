from os import getenv, path

import sqlalchemy.dialects.postgresql
from dotenv import load_dotenv
from firebase_admin import credentials, initialize_app
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# Load the environment variables
load_dotenv()

# Create the database engine
engine = create_engine(getenv("DATABASE_URL"))
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# Firebase configuration
key_path = path.join(path.dirname(__file__), "service_account_key.json")

cred = credentials.Certificate(key_path)
initialize_app(cred, {"storageBucket": getenv("STORAGE_BUCKET")})
