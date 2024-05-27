package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

public final class Canjeo {
  @Getter
  @Setter
  private int id;
  @Getter
  private final Colaborador colaborador;
  @Getter
  private final Recompensa recompensa;
  private final ZonedDateTime fecha;

  public Canjeo(Colaborador colaborador, Recompensa recompensa, ZonedDateTime fecha) {
    this.colaborador = colaborador;
    this.recompensa = recompensa;
    this.fecha = fecha;
  }
}