package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import lombok.NonNull;

public class OfertaRecompensa extends Contribucion {
  final @NonNull Recompensa recompensa;

  public OfertaRecompensa(@NonNull Colaborador colaborador, @NonNull Recompensa recompensa) {
    super(colaborador);
    this.recompensa = recompensa;
  }
}

