from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
import ProductModel, schemas
from database import SessionLocal, engine, Base

import json


Base.metadata.create_all(bind=engine)

app = FastAPI()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@app.post("/products/create", response_model=schemas.ProductResponse)
def create_product(product: schemas.ProductCreate, db: Session = Depends(get_db)):

    db_product = ProductModel.Product(
        ean=product.ean,
        title=product.title,
        lowest_recorded_price=product.lowest_recorded_price,
    )

    # Usando set_images para converter a lista de imagens em string JSON
    db_product.set_images(product.images)

    db.add(db_product)
    db.commit()
    db.refresh(db_product)

    return db_product


@app.get('/products/findAll', response_model=list)
def list_all(db: Session = Depends(get_db)):
    products = db.query(ProductModel.Product).all()

    return [
        schemas.ProductResponse(
            ean=p.ean,
            title=p.title,
            lowest_recorded_price=p.lowest_recorded_price,
            images=p.images
        )

        for p in products
    ]

@app.delete('/products/delete/{ean}', response_model=dict)
def delete_item(ean: str, db: Session = Depends(get_db)):
    product = db.query(ProductModel.Product).filter(ProductModel.Product.ean == ean).first()

    if product is None:
        raise HTTPException(status_code=404, detail="Produto não encontrado")
    

    db.delete(product)
    db.commit()

    return {"message": "Produto deletado com sucesso"}
