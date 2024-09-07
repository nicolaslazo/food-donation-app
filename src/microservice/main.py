from fastapi import Depends, FastAPI
from sqlalchemy.orm import Session
from sqlalchemy import func

from .db import get_db
from . import models
from . import schemas

app = FastAPI()


@app.get("/personas-por-barrio", response_model=schemas.ReporteBarrios)
def personas_por_barrio(db: Session = Depends(get_db)):
    pass
