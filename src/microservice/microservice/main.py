from collections import defaultdict

from db import get_db
from fastapi import Depends, FastAPI
from models import Heladera, SolicitudAperturaPorConsumicion, Tarjeta, Usuario
from sqlalchemy.orm import Session

app = FastAPI()


@app.get("/personas-por-barrio")
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
