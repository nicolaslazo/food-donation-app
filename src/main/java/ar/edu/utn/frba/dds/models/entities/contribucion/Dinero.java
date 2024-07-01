package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Dinero extends Contribucion {
  @Getter
  final float monto;
  final Integer frecuenciaEnDias;
  @Getter
  @Setter
  int id;

  public Dinero(@NonNull Colaborador colaborador, float monto, Integer frecuenciaEnDias) {
    super(colaborador);
    this.monto = monto;
    this.frecuenciaEnDias = frecuenciaEnDias;
  }

  @Override
  public String toString() {
    return "Dinero{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        ", monto=" + monto +
        '}';
  }
}
