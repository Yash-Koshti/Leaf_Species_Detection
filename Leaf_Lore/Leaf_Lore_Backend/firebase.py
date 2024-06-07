from os import path

from firebase_admin import storage


class Firebase:
    def __init__(self):
        self.bucket = storage.bucket()
        self.local_images_path = path.join(path.dirname(__file__), "images")

    def download_from(self, directory_name: str, img_name: str):
        blob = self.bucket.blob(f"{directory_name}/{img_name}")
        blob.download_to_filename(path.join(self.local_images_path, img_name))
