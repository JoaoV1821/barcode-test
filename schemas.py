from pydantic import BaseModel
from typing import List, Optional

class ProductBase(BaseModel):
    ean: str
    title: str
    lowest_recorded_price: Optional[float]
    images: List[str]

class ProductCreate(ProductBase):
    pass

class ProductResponse(ProductBase):
    class Config:
        orm_mode = True
