package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

public final class Canjeo {
  @NonNull
  @Getter
  private final Colaborador colaborador;
  @NonNull
  @Getter
  private final Recompensa recompensa;
  @NonNull
  private final ZonedDateTime fecha;
  @Getter
  @Setter
  private int id;

  public Canjeo(Colaborador colaborador, Recompensa recompensa, ZonedDateTime fecha) {
    this.colaborador = colaborador;
    this.recompensa = recompensa;
    this.fecha = fecha;
  }
}