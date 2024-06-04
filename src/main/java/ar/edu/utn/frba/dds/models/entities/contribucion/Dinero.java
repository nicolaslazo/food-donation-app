package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

public class Dinero extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @Getter
  private final float monto;
  private final Integer frecuenciaEnDias;
  @Getter
  @Setter
  private int id;

  public Dinero(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, float monto, Integer frecuenciaEnDias) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.monto = monto;
    this.frecuenciaEnDias = frecuenciaEnDias;
  }

  @Override
  public String toString() {
    return "Dinero{" +
        "colaborador=" + colaborador +
        ", fecha=" + fecha +
        ", monto=" + monto +
        '}';
  }
}
