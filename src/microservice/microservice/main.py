from db import get_db
from fastapi import Depends, FastAPI
from models import Heladera, SolicitudAperturaPorConsumicion, Tarjeta, Usuario
from sqlalchemy.orm import Session

app = FastAPI()


@app.get("/")
def hello_world():
    return {"Hello": "World"}


@app.get("/usuarios")
def usuarios(db: Session = Depends(get_db)):
    return db.query(Usuario).all()


@app.get("/solicitudes")
def solicitudes(db: Session = Depends(get_db)):
    return db.query(SolicitudAperturaPorConsumicion).all()


@app.get("/heladeras")
def heladeras(db: Session = Depends(get_db)):
    return db.query(Heladera).all()


# @app.get("/personas-por-barrio", response_model=schemas.ReporteBarrios)
@app.get("/personas-por-barrio")
def personas_por_barrio(db: Session = Depends(get_db)):
    resultados = (
        db.query(SolicitudAperturaPorConsumicion, Tarjeta, Usuario, Heladera)
        .filter(SolicitudAperturaPorConsumicion.fechaUsada.isnot(None))
        .join(Tarjeta, Tarjeta.id == SolicitudAperturaPorConsumicion.idTarjeta)
        .join(Usuario, Usuario.id == Tarjeta.idRecipiente)
        .join(Heladera, Heladera.id == SolicitudAperturaPorConsumicion.idHeladera)
        .all()
    )

    return [
        {
            "solicitud": solicitud.as_dict(),
            "tarjeta": tarjeta.as_dict(),
            "usuario": usuario.as_dict(),
            "heladera": heladera.as_dict(),
        }
        for solicitud, tarjeta, usuario, heladera in resultados
    ]
