package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

public abstract class MovimientoViandas extends Contribucion {
  @Getter
  final @NonNull List<Vianda> viandas;
  final Heladera destino;
  @Getter
  @Setter
  private int id;

  public MovimientoViandas(@NonNull Colaborador colaborador, List<Vianda> viandas, Heladera heladera) {
    super(colaborador);

    this.viandas = viandas;
    this.destino = heladera;
  }

  public int getNumeroViandas() {
    return viandas.size();
  }
}
