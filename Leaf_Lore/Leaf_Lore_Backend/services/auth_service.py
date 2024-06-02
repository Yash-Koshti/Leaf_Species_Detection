from datetime import datetime, timedelta
from os import getenv

from dotenv import load_dotenv
from jose import jwt
from passlib.context import CryptContext
from schemas import UserSchema
from sqlalchemy.orm import Session

load_dotenv()

SECRET_KEY = getenv("SECRET_KEY")
ALGORITHM = getenv("ALGORITHM")
ACCESS_TOKEN_EXPIRES = timedelta(minutes=int(getenv("ACCESS_TOKEN_EXPIRE_MINUTES")))

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


class AuthService:
    def __init__(self, db: Session):
        self.db = db

    def authenticate_user(self, username: str, password: str) -> UserSchema:
        user = self.db.query(UserSchema).filter(UserSchema.name == username).first()
        if not user:
            return None
        if not pwd_context.verify(password, user.password):
            return None
        return user

    def create_access_token(self, data: dict):
        to_encode = data.copy()
        expire = datetime.now() + ACCESS_TOKEN_EXPIRES
        to_encode.update({"exp": expire})
        encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
        return encoded_jwt
