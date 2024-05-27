package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.List;

public class RedistribucionViandas extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  private final MotivoDeDistribucion motivo;
  private final Heladera origen;
  private final Heladera destino;
  @NonNull
  @Getter
  private final List<Vianda> viandas;

  public RedistribucionViandas(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha,
                               MotivoDeDistribucion motivo, Heladera origen, Heladera destino,
                               @NonNull List<Vianda> viandas) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
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
        ", fecha=" + fecha +
        ", cantidadViandas=" + viandas.size() +
        '}';
  }
}
