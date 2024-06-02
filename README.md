# Leaf_Species_Detection

# Migrations

### How to create
```
alembic revision --autogenerate -m "<your_message>"
```
- This will create a new migration.
- It will revise the changes needed to do in DB.
- This is similar to staging changes in git.

### How to apply changes
```
alembic upgrade head
```
- This will apply the latest changes.
- You can even rollback from here.

### How to rollback
```
alembic downgrade head
```
- This will downgrade the db to a migration back.
- This will apply the changes which were in the just previous revision.