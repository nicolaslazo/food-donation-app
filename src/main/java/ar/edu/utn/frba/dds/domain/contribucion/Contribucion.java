package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.Objects;

public abstract class Contribucion {
  @NonNull
  @Getter
  private final Colaborador colaborador;
  @NonNull
  @Getter
  private final ZonedDateTime fecha;

  public Contribucion(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha) {
    this.colaborador = colaborador;
    this.fecha = fecha;

    colaborador.getContribuciones().add(this);
  }
}