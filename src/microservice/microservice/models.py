from uuid import UUID

from sqlalchemy import (
    BINARY,
    Column,
    DateTime,
    ForeignKey,
    Integer,
    String,
)
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy.types import TypeDecorator

Base = declarative_base()


class Uuid(TypeDecorator):
    impl = BINARY(16)

    def process_bind_param(self, value, dialect):
        if value is None:
            return value
        if dialect.name == "mysql":
            return value.bytes
        else:
            return value

    def process_result_value(self, value, dialect):
        if value is None:
            return value
        return UUID(bytes=value)


class SolicitudAperturaPorConsumicion(Base):
    __tablename__ = "solicitudAperturaPorConsumicion"

    id = Column(Integer, primary_key=True)
    idTarjeta = Column(Uuid, ForeignKey("tarjeta.id"), nullable=False)
    idHeladera = Column(Integer, ForeignKey("heladera.id"), nullable=False)
    fechaUsada = Column(DateTime)

    tarjeta = relationship("Tarjeta", back_populates="solicitudes")

    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}


class Tarjeta(Base):
    __tablename__ = "tarjeta"

    id = Column(Integer, primary_key=True)
    idRecipiente = Column(Integer, ForeignKey("usuario.id"))

    recipiente = relationship("Usuario", foreign_keys=[idRecipiente])
    solicitudes = relationship(
        "SolicitudAperturaPorConsumicion", back_populates="tarjeta"
    )

    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}


class Usuario(Base):
    __tablename__ = "usuario"

    id = Column(Integer, primary_key=True)
    primerNombre = Column(String, nullable=False)
    apellido = Column(String, nullable=False)

    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}


class Heladera(Base):
    __tablename__ = "heladera"

    id = Column(Integer, primary_key=True)
    nombre = Column(String, nullable=False)
    barrio = Column(String, nullable=False)

    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}
