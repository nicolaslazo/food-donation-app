package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

public class CuidadoHeladera extends Contribucion {
  private final ZonedDateTime fechaContribucion;

  @Getter
  private final Heladera heladera;

  public CuidadoHeladera(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, ZonedDateTime fechaContribucion, Heladera heladera) {
    super(colaborador, fecha);
    this.fechaContribucion = fechaContribucion;
    this.heladera = heladera;
  }
}
