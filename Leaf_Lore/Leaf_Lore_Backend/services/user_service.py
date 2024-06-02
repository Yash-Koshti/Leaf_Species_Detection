from datetime import datetime
from uuid import UUID

from fastapi.logger import logger
from models import User
from schemas import UserSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class UserService:
    def __init__(self, db: Session):
        self.db = db

    def register(self, user: User) -> UserSchema | None:
        user = UserSchema(
            name=user.name, email=user.email, password=user.password, role=user.role
        )
        try:
            self.db.add(user)
            self.db.commit()
            self.db.refresh(user)
            return user
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating user: {e}")
            return None

    def login(self, user: User) -> UserSchema:
        return self.db.query(UserSchema).filter(UserSchema.name == user.name).first()

    def get_user_by_id(self, user_id: UUID) -> UserSchema:
        return self.db.query(UserSchema).filter(UserSchema.id == user_id).first()

    def update_user(self, user: User) -> UserSchema:
        cur_user = self.get_user_by_id(user.id)
        if cur_user:
            cur_user.name = user.name
            cur_user.email = user.email
            cur_user.password = user.password
            cur_user.role = user.role
            cur_user.updated_at = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            self.db.commit()
            self.db.refresh(cur_user)
        return cur_user

    def delete_user(self, user: User) -> UserSchema:
        user = self.get_user_by_id(user.id)
        if user:
            self.db.delete(user)
            self.db.commit()
        return user
