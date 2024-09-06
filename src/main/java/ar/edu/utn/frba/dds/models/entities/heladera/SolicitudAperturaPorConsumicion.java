package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.NonNull;

import java.time.ZonedDateTime;

public class SolicitudAperturaPorConsumicion {
  @NonNull Tarjeta tarjeta;
  @NonNull Vianda vianda;
  @NonNull ZonedDateTime fechaCreacion;
  @NonNull ZonedDateTime fechaVencimiento;
  ZonedDateTime fechaUsada = null;

  public SolicitudAperturaPorConsumicion(@NonNull Tarjeta tarjeta,
                                         @NonNull Vianda vianda,
                                         @NonNull ZonedDateTime fechaCreacion) {
    this.tarjeta = tarjeta;
    this.vianda = vianda;
    this.fechaCreacion = fechaCreacion;

    int tiempoSolicitudAperturaMinutos =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }

  void verificarPrecondicionesApertura(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    if (fechaUsada != null)
      throw new SolicitudInvalidaException("Esta solicitud ya fue usada en" + fechaUsada);
    if (timestamp.isAfter(fechaVencimiento)) throw new SolicitudInvalidaException("Esta solicitud ya venció");
    if (timestamp.isBefore(fechaCreacion))
      throw new SolicitudInvalidaException("No se debería poder usar una solicitud antes de que entre en vigencia");
  }

  public void setFechaUsada(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    verificarPrecondicionesApertura(timestamp);

    vianda.setHeladera(null);
    fechaUsada = timestamp;
  }
}
