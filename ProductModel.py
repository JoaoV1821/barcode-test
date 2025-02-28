from sqlalchemy import Column, String, Float, Integer
from database import Base
from sqlalchemy.ext.mutable import MutableList
from sqlalchemy.types import JSON

class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True, index=True)
    ean = Column(String, unique=True, index=True)
    title = Column(String)
    lowest_recorded_price = Column(Float, nullable=True)  # Pode ser None
    images =  Column(MutableList.as_mutable(JSON))
