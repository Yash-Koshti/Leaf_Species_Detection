from repositories.user_repository import UserRepository
from models import User
from schemas import UserSchema


class UserService:
    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def register(self, user: User) -> UserSchema | None:
        user = UserSchema(
            name=user.name, email=user.email, password=user.password, role=user.role
        )
        return self.user_repository.create_user(user)

    def login(self, user: User) -> UserSchema:
        return self.user_repository.get_user(user.email, user.password, user.role)

    def update_user(self, user: User) -> UserSchema:
        return self.user_repository.update_user(user)

    def delete_user(self, user: User) -> UserSchema:
        return self.user_repository.delete_user(user.id)
