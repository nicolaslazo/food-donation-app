package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;

import java.util.Collection;

public class RedistribucionViandas extends MovimientoViandas {
  // Algunos de estos se mantienen nulificables para el cargador masivo CSV
  @Getter
  final Heladera origen;
  final MotivoDeDistribucion motivo;

  public RedistribucionViandas(Colaborador colaborador,
                               Collection<Vianda> viandas,
                               Heladera destino,
                               Heladera origen,
                               MotivoDeDistribucion motivo) {
    super(colaborador, viandas, destino);
    this.origen = origen;
    this.motivo = motivo;
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
