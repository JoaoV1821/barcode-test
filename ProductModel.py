from sqlalchemy import Column, String, Float, Integer
from sqlalchemy.ext.mutable import MutableDict
from sqlalchemy.types import String
from database import Base
import json

class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True, index=True)
    ean = Column(String, unique=True, index=True)
    title = Column(String)
    lowest_recorded_price = Column(Float, nullable=True)
    images = Column(String)  # Usando String para armazenar a lista JSON como texto

    def set_images(self, images_list):
        self.images = json.dumps(images_list)  # Armazena a lista como uma string JSON

    def get_images(self):
        return json.loads(self.images) if self.images else []  # Converte a string JSON de volta para uma lista

