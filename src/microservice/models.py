from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, BINARY
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class Usuario(Base):
    __tablename__ = "usuario"

    id = Column(BINARY(16), primary_key=True, index=True)
    documento = Column(String, unique=True, nullable=False)
    primer_nombre = Column(String, nullable=False)
    apellido = Column(String, nullable=False)
    fecha_nacimiento = Column(DateTime)
    contrasenia = Column(String, nullable=False)


class PersonaVulnerable(Base):
    __tablename__ = "personaVulnerable"

    id = Column(BINARY(16), ForeignKey("usuario.id"), primary_key=True)
    id_reclutador = Column(BINARY(16), ForeignKey("usuario.id"), nullable=False)
    fecha_registro = Column(DateTime, nullable=False)
    menores_a_cargo = Column(Integer, nullable=False)
    usuario = relationship("Usuario", foreign_keys=[id])
    reclutador = relationship("Usuario", foreign_keys=[id_reclutador])


class DireccionResidencia(Base):
    __tablename__ = "direccionResidencia"

    id = Column(BINARY(16), ForeignKey("usuario.id"), primary_key=True)
    unidad = Column(String)
    piso = Column(String)
    numero_de_casa = Column(String, nullable=False)
    calle = Column(String, nullable=False)
    codigo_postal = Column(String, nullable=False)
    barrio = Column(String, nullable=False)
    ciudad = Column(String, nullable=False)
    provincia = Column(String, nullable=False)
    pais = Column(String, nullable=False)
    usuario = relationship("Usuario")


class Tarjeta(Base):
    __tablename__ = "tarjeta"

    id = Column(BINARY(16), primary_key=True, index=True)
    id_proveedor = Column(BINARY(16), ForeignKey("usuario.id"))
    id_recipiente = Column(BINARY(16), ForeignKey("usuario.id"))
    fecha_alta = Column(DateTime)
    fecha_baja = Column(DateTime)
    id_responsable_baja = Column(BINARY(16), ForeignKey("usuario.id"))
    proveedor = relationship("Usuario", foreign_keys=[id_proveedor])
    recipiente = relationship("Usuario", foreign_keys=[id_recipiente])
    responsable_de_baja = relationship("Usuario", foreign_keys=[id_responsable_baja])


class SolicitudAperturaPorConsumicion(Base):
    __tablename__ = "solicitudAperturaPorConsumicion"

    id = Column(Integer, primary_key=True, autoincrement=True)
    tarjeta_id = Column(BINARY(16), ForeignKey("tarjeta.id"), nullable=False)
    vianda_id = Column(BINARY(16), nullable=False)  # Asumimos que Vianda es transient
    fecha_creacion = Column(DateTime, nullable=False)
    fecha_vencimiento = Column(DateTime, nullable=False)
    fecha_usada = Column(DateTime)
    tarjeta = relationship("Tarjeta")


# Clases transient
class Colaborador:
    pass


class Vianda:
    pass


class Rol:
    pass


class Permiso:
    pass
