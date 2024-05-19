from datetime import datetime

from fastapi.logger import logger
from models import User
from schemas import UserSchema
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import Session


class UserRepository:
    def __init__(self, db: Session):
        self.db = db
    
    def create_user(self, user: UserSchema) -> UserSchema | None:
        try:
            self.db.add(user)
            self.db.commit()
            self.db.refresh(user)
            return user
        except SQLAlchemyError as e:
            self.db.rollback()
            logger.error(f"Error occurred while creating user: {e}")
            return None
    
    def get_user(self, email: str, password: str, role: str) -> UserSchema:
        return self.db.query(UserSchema).filter(UserSchema.email == email, UserSchema.password == password, UserSchema.role == role).first()

    def get_user_by_id(self, user_id: int) -> UserSchema:
        return self.db.query(UserSchema).filter(UserSchema.id == user_id).first()

    def update_user(self, user_data: User) -> UserSchema:
        cur_user = self.get_user_by_id(user_data.id)
        if cur_user:
            cur_user.name = user_data.name
            cur_user.email = user_data.email
            cur_user.password = user_data.password
            cur_user.role = user_data.role
            cur_user.updated_at = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            self.db.commit()
            self.db.refresh(cur_user)
        return cur_user

    def delete_user(self, user_id: int) -> UserSchema:
        user = self.get_user_by_id(user_id)
        if user:
            self.db.delete(user)
            self.db.commit()
        return user