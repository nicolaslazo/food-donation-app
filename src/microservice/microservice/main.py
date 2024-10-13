from collections import defaultdict
from typing import List

from fastapi import Depends, FastAPI
from pydantic import BaseModel, Field
from sqlalchemy.orm import Session

from db import get_db
from models import Heladera, SolicitudAperturaPorConsumicion, Tarjeta, Usuario

app = FastAPI()


class PersonaPorBarrioResponse(BaseModel):
    barrio: str = Field(..., description="Nombre del barrio")
    cantidad: int = Field(..., description="Cantidad de personas en el barrio")
    nombres: List[str] = Field(
        ..., description="Lista de nombres completos de las personas en el barrio"
    )

    class Config:
        json_schema_extra = {
            "example": {
                "barrio": "Constitución",
                "cantidad": 5,
                "nombres": [
                    "Juan Perez",
                    "Maria Gomez",
                    "Carlos Lopez",
                    "Ana Martinez",
                    "Luis Rodriguez",
                ],
            }
        }


@app.get(
    "/personas-por-barrio",
    response_model=List[PersonaPorBarrioResponse],
    summary="Lista las personas que solicitaron al menos una vianda, agrupadas por el barrio donde lo hicieron",
    description="Lista las personas que solicitaron al menos una vianda, agrupadas por el barrio donde lo hicieron",
)
def personas_por_barrio(db: Session = Depends(get_db)):
    resultados = (
        db.query(Heladera.barrio, Usuario.primerNombre, Usuario.apellido)
        .filter(SolicitudAperturaPorConsumicion.fechaUsada.isnot(None))
        .join(
            SolicitudAperturaPorConsumicion,
            SolicitudAperturaPorConsumicion.idHeladera == Heladera.id,
        )
        .join(Tarjeta, Tarjeta.id == SolicitudAperturaPorConsumicion.idTarjeta)
        .join(Usuario, Usuario.id == Tarjeta.idRecipiente)
        .all()
    )

    agrupados = defaultdict(list)
    for barrio, primer_nombre, apellido in resultados:
        agrupados[barrio].append(f"{primer_nombre} {apellido}")

    return [
        {"barrio": barrio, "cantidad": len(nombres), "nombres": nombres}
        for barrio, nombres in agrupados.items()
    ]
