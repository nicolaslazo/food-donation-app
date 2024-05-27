package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import lombok.NonNull;

import java.time.ZonedDateTime;

public class OfertaRecompensa extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @NonNull
  private final Recompensa recompensa;

  public OfertaRecompensa(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, @NonNull Recompensa recompensa) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.recompensa = recompensa;
  }
}

