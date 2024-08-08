package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Collection;

public class SolicitudAperturaPorContribucion {
  @Getter
  final @NonNull Tarjeta tarjeta;
  final @NonNull MovimientoViandas razon;
  @Getter
  final @NonNull ZonedDateTime fechaCreacion;
  @Getter
  final @NonNull ZonedDateTime fechaVencimiento;
  @Getter
  ZonedDateTime fechaAperturaEnOrigen = null;
  @Getter
  ZonedDateTime fechaAperturaEnDestino = null;
  @Getter
  @Setter
  int id;

  public SolicitudAperturaPorContribucion(Tarjeta tarjeta,
                                          MovimientoViandas razon,
                                          ZonedDateTime fechaCreacion) {
    this.tarjeta = tarjeta;
    this.razon = razon;
    this.fechaCreacion = fechaCreacion;

    int tiempoSolicitudAperturaMinutos =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }

  // Cubre los casos comunes entre las donaciones y las redistribuciones
  void verificarPrecondicionesApertura(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    if (fechaAperturaEnDestino != null)
      throw new SolicitudInvalidaException("Esta solicitud ya fue usada en" + fechaAperturaEnDestino);
    if (timestamp.isAfter(fechaVencimiento)) throw new SolicitudInvalidaException("Esta solicitud ya venció");
    if (timestamp.isBefore(fechaCreacion))
      throw new SolicitudInvalidaException("No se debería poder usar una solicitud antes de que entre en vigencia");
  }

  public void setFechaAperturaEnOrigen(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    if (razon instanceof DonacionViandas)
      throw new SolicitudInvalidaException("Una donación de viandas no puede tomar viandas de una heladera origen");
    if (fechaAperturaEnOrigen != null)
      throw new SolicitudInvalidaException("Esta solicitud ya fue usada en " + fechaAperturaEnOrigen);
    verificarPrecondicionesApertura(timestamp);

    fechaAperturaEnOrigen = timestamp;
  }

  public void setFechaAperturaEnDestino(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    verificarPrecondicionesApertura(timestamp);

    razon.setFechaRealizada(timestamp);
    fechaAperturaEnDestino = timestamp;
  }

  public boolean isUsada(boolean paraExtraccion) {
    return (paraExtraccion && fechaAperturaEnOrigen != null) || fechaAperturaEnDestino != null;
  }

  public boolean isVigenteAlMomento(ZonedDateTime momento, boolean paraExtraccion) {
    if (paraExtraccion && razon instanceof DonacionViandas) return false;

    return !isUsada(paraExtraccion) && momento.isAfter(fechaCreacion) && momento.isBefore(fechaVencimiento);
  }

  public boolean isVigente(boolean paraExtraccion) {
    return isVigenteAlMomento(ZonedDateTime.now(), paraExtraccion);
  }

  public Collection<Vianda> getViandas() {
    return razon.getViandas();
  }

  public double[] getPesosDeViandasEnGramos() {
    return this.getViandas().stream().mapToDouble(Vianda::getPesoEnGramos).toArray();
  }

  public Heladera getHeladeraDestino() {
    return razon.getDestino();
  }
}
