package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import java.util.List;

public class RedistribucionViandas extends MovimientoViandas {
  // Algunos de estos se mantienen nulificables para el cargador masivo CSV
  final Heladera origen;
  final MotivoDeDistribucion motivo;

  public RedistribucionViandas(Colaborador colaborador,
                               List<Vianda> viandas,
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
