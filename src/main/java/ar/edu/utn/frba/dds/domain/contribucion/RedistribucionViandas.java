package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.Heladera;
import ar.edu.utn.frba.dds.domain.Vianda;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
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
}
