package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

public class CuidadoHeladera extends Contribucion {
  private final Colaborador colaborador;
  private final ZonedDateTime fecha;
  @Getter
  private final Heladera heladera;
  @Getter
  @Setter
  private int id;

  public CuidadoHeladera(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, Heladera heladera) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.heladera = heladera;
  }
}
