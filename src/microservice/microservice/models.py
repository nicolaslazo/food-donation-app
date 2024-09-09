from enum import Enum as PyEnum
from uuid import UUID

from sqlalchemy import (
    BINARY,
    Column,
    DateTime,
    Enum,
    Float,
    ForeignKey,
    Integer,
    String,
    UniqueConstraint,
)
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import composite, relationship
from sqlalchemy.types import TypeDecorator

Base = declarative_base()


class Uuid(TypeDecorator):
    impl = BINARY(16)

    def process_bind_param(self, value, dialect):
        if value is None:
            return value
        if dialect.name == 'mysql':
            return value.bytes
        else:
            return value

    def process_result_value(self, value, dialect):
        if value is None:
            return value
        return UUID(bytes=value)

class Permiso(Base):
    __tablename__ = "permiso"

    id = Column(Uuid, primary_key=True)
    nombre = Column(String, unique=True, nullable=False)
    descripcion = Column(String)


class SolicitudAperturaPorConsumicion(Base):
    __tablename__ = "solicitudAperturaPorConsumicion"

    id = Column(Integer, primary_key=True)
    idTarjeta = Column(Uuid, ForeignKey("tarjeta.id"), nullable=False)
    idVianda = Column(Integer, ForeignKey("vianda.id"), nullable=False)
    fechaCreacion = Column(DateTime, nullable=False)
    fechaVencimiento = Column(DateTime, nullable=False)
    fechaUsada = Column(DateTime)

    tarjeta = relationship("Tarjeta", back_populates="solicitudes")
    vianda = relationship("Vianda", back_populates="solicitudes")


class Tarjeta(Base):
    __tablename__ = "tarjeta"

    id = Column(Uuid, primary_key=True)
    idProveedor = Column(Uuid, ForeignKey("colaborador.idUsuario"))
    idRecipiente = Column(Uuid, ForeignKey("usuario.id"))
    fechaAlta = Column(DateTime)
    fechaBaja = Column(DateTime)
    idResponsableBaja = Column(Uuid, ForeignKey("usuario.id"))

    proveedor = relationship("Colaborador", foreign_keys=[idProveedor])
    recipiente = relationship("Usuario", foreign_keys=[idRecipiente])
    responsableDeBaja = relationship("Usuario", foreign_keys=[idResponsableBaja])
    solicitudes = relationship(
        "SolicitudAperturaPorConsumicion", back_populates="tarjeta"
    )


class Vianda(Base):
    __tablename__ = "vianda"

    id = Column(Integer, primary_key=True)
    descripcion = Column(String, nullable=False)
    fechaCaducidad = Column(DateTime, nullable=False)
    fechaDonacion = Column(DateTime, nullable=False)
    idColaborador = Column(Uuid, ForeignKey("colaborador.idUsuario"), nullable=False)
    pesoEnGramos = Column(Float, nullable=False)
    caloriasVianda = Column(Integer, nullable=False)
    idHeladera = Column(Integer, ForeignKey("heladera.id"))

    colaborador = relationship("Colaborador")
    heladera = relationship("Heladera")
    solicitudes = relationship(
        "SolicitudAperturaPorConsumicion", back_populates="vianda"
    )


class Colaborador(Base):
    __tablename__ = "colaborador"

    idUsuario = Column(Uuid, ForeignKey("usuario.id"), primary_key=True)
    latitud = Column(Float)
    longitud = Column(Float)

    usuario = relationship("Usuario", back_populates="colaborador")


class TipoDocumento(PyEnum):
    DNI = "DNI"
    LIBRETA_CIVICA = "LC"
    LIBRETA_DE_ENROLAMIENTO = "LE"

    @classmethod
    def from_string(cls, tipo):
        for member in cls:
            if member.value == tipo:
                return member
        return None


class Documento(object):
    def __init__(self, tipo, valor):
        self.tipo = tipo
        self.valor = valor

    def __composite_values__(self):
        return self.tipo, self.valor

    def __eq__(self, other):
        return (
            isinstance(other, Documento)
            and other.tipo == self.tipo
            and other.valor == self.valor
        )

    def __ne__(self, other):
        return not self.__eq__(other)


class Usuario(Base):
    __tablename__ = "usuario"

    id = Column(Uuid, primary_key=True)
    documento_tipo = Column("tipo", Enum(TipoDocumento), nullable=False)
    documento_valor = Column("valor", Integer, nullable=False)
    documento = composite(Documento, documento_tipo, documento_valor)
    primerNombre = Column(String, nullable=False)
    apellido = Column(String, nullable=False)
    fechaNacimiento = Column(DateTime)
    contrasenia = Column(String, nullable=False)

    colaborador = relationship("Colaborador", back_populates="usuario", uselist=False)

    __table_args__ = (UniqueConstraint("tipo", "valor", name="uq_documento"),)


class Rol(Base):
    __tablename__ = "rol"

    id = Column(Uuid, primary_key=True)
    nombre = Column(String, unique=True, nullable=False)


class Heladera(Base):
    __tablename__ = "heladera"

    id = Column(Integer, primary_key=True)
    nombre = Column(String, nullable=False)
    capacidadEnViandas = Column(Integer, nullable=False)
    fechaInstalacion = Column(DateTime, nullable=False)
    idColaborador = Column(Uuid, ForeignKey("colaborador.idUsuario"), nullable=False)
    latitud = Column(Float)
    longitud = Column(Float)
    ultimaTemperaturaRegistradaEnCelsius = Column(Float)
    momentoDeUltimaTempRegistrada = Column(DateTime)

    encargado = relationship("Colaborador")
