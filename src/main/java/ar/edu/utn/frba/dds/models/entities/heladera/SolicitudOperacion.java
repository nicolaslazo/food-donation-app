package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
public class SolicitudOperacion {
  final @NonNull Colaborador colaborador;
  final @NonNull Heladera heladera;
  final @NonNull ZonedDateTime fechaCreacion;
  final @NonNull ZonedDateTime fechaVencimiento;
  @Setter
  int id;

  public SolicitudOperacion(@NonNull Colaborador colaborador, @NonNull Heladera heladera, @NonNull ZonedDateTime fechaCreacion) {
    int tiempoSolicitudAperturaMinutos = Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.colaborador = colaborador;
    this.heladera = heladera;
    this.fechaCreacion = fechaCreacion;
    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }
}
