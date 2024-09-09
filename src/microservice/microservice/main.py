import models
from db import get_db
from fastapi import Depends, FastAPI
from sqlalchemy.orm import Session

app = FastAPI()


@app.get("/")
def hello_world():
    return {"Hello": "World"}


@app.get("/usuarios")
def usuarios(db: Session = Depends(get_db)):
    return db.query(models.Usuario).all()


@app.get("/solicitudes")
def solicitudes(db: Session = Depends(get_db)):
    return db.query(models.SolicitudAperturaPorConsumicion).all()


# @app.get("/personas-por-barrio", response_model=schemas.ReporteBarrios)
@app.get("/personas-por-barrio")
def personas_por_barrio(db: Session = Depends(get_db)):
    return db.query(models.SolicitudAperturaPorConsumicion).all()
