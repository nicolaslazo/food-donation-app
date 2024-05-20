package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.Vianda;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.List;

public class DonacionViandas extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @NonNull
  @Getter
  private final List<Vianda> viandas;

  public DonacionViandas(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, @NonNull List<Vianda> viandas) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.viandas = viandas;
  }
}
