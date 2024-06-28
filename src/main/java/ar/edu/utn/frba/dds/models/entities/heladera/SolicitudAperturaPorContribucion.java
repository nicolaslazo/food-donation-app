package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
public class SolicitudAperturaPorContribucion {
  final @NonNull Tarjeta tarjeta;
  final @NonNull MovimientoViandas razon;
  final @NonNull ZonedDateTime fechaCreacion;
  final @NonNull ZonedDateTime fechaVencimiento;
  @Setter
  int id;

  public SolicitudAperturaPorContribucion(Tarjeta tarjeta,
                                          MovimientoViandas razon,
                                          ZonedDateTime fechaCreacion) {
    int tiempoSolicitudAperturaMinutos =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.tarjeta = tarjeta;
    this.razon = razon;
    this.fechaCreacion = fechaCreacion;
    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }
}
