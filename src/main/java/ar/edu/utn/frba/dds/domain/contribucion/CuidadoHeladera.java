package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.heladera.Heladera;
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
