package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Getter
public abstract class Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;

  public Contribucion(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha) {
    this.colaborador = colaborador;
    this.fecha = fecha;

    colaborador.getContribuciones().add(this);
  }

  @Override
  public String toString() {
    return "Contribucion{" +
        "colaborador=" + colaborador +
        ", fecha=" + fecha +
        '}';
  }
}