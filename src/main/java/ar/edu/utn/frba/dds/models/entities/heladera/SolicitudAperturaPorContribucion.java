package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
public class SolicitudAperturaPorContribucion {
  final @NonNull Colaborador colaborador;
  final @NonNull Heladera heladera;
  final @NonNull MovimientoViandas razon;
  final @NonNull ZonedDateTime fechaCreacion;
  final @NonNull ZonedDateTime fechaVencimiento;
  @Setter
  int id;

  public SolicitudAperturaPorContribucion(Colaborador colaborador,
                                          Heladera heladera,
                                          MovimientoViandas razon,
                                          ZonedDateTime fechaCreacion) {
    int tiempoSolicitudAperturaMinutos =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.colaborador = colaborador;
    this.heladera = heladera;
    this.razon = razon;
    this.fechaCreacion = fechaCreacion;
    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }
}
