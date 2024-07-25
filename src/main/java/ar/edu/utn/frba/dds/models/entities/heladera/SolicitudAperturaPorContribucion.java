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
  ZonedDateTime fechaUsada = null;
  @Getter
  @Setter
  int id;

  public SolicitudAperturaPorContribucion(Tarjeta tarjeta,
                                          DonacionViandas razon,
                                          ZonedDateTime fechaCreacion) {
    int tiempoSolicitudAperturaMinutos =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.tarjeta = tarjeta;
    this.razon = razon;
    this.fechaCreacion = fechaCreacion;
    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }

  public void setFechaUsada(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    if (fechaUsada != null) throw new SolicitudInvalidaException("Esta solicitud ya fue usada en" + fechaUsada);
    if (timestamp.isAfter(fechaVencimiento)) throw new SolicitudInvalidaException("Esta solicitud ya venció");
    if (timestamp.isBefore(fechaCreacion))
      throw new SolicitudInvalidaException("No se debería poder usar una solicitud antes de que entre en vigencia");

    razon.setFechaRealizada(timestamp);
    fechaUsada = timestamp;
  }

  public boolean isUsada() {
    return fechaUsada != null;
  }

  public boolean isVigenteAlMomento(ZonedDateTime momento) {
    return !isUsada() && momento.isAfter(fechaCreacion) && momento.isBefore(fechaVencimiento);
  }

  public boolean isVigente() {
    return isVigenteAlMomento(ZonedDateTime.now());
  }

  public Collection<Vianda> getViandas() {
    return razon.getViandas();
  }

  public double[] getPesosDeViandasEnGramos() {
    return this.getViandas().stream().mapToDouble(Vianda::getPesoEnGramos).toArray();
  }

  public Heladera getHeladera() {
    // TODO: eventualmente va a tener que ser un origen y no un destino
    return razon.getDestino();
  }
}
