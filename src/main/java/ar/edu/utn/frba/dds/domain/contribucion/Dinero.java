package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

public class Dinero extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @NonNull
  @Getter
  private final float monto;
  private final int frecuenciaEnDias;

  public Dinero(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, @NonNull float monto, int frecuenciaEnDias) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.monto = monto;
    this.frecuenciaEnDias = frecuenciaEnDias;
  }
}
