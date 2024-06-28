package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

public class RedistribucionViandas extends Contribucion {
  // Algunos de estos se mantienen nulificables para el cargador masivo CSV
  final MotivoDeDistribucion motivo;
  final Heladera origen;
  final Heladera destino;
  @Getter
  final @NonNull List<Vianda> viandas;
  @Getter
  @Setter
  int id;

  public RedistribucionViandas(Colaborador colaborador, MotivoDeDistribucion motivo, Heladera origen, Heladera destino,
                               List<Vianda> viandas) {
    super(colaborador);
    this.motivo = motivo;
    this.origen = origen;
    this.destino = destino;
    this.viandas = viandas;
  }

  public int getNumeroViandas() {
    return viandas.size();
  }

  @Override
  public String toString() {
    return "RedistribucionViandas{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        ", cantidadViandas=" + viandas.size() +
        '}';
  }
}
