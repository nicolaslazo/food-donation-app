package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
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
}