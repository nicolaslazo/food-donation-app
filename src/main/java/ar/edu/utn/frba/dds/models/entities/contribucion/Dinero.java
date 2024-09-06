package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "donacionDinero")
public class Dinero extends Contribucion {

  @Column(name = "monto", updatable = false)
  @Getter
  Double monto;

  @Column(name = "frecuenciaEnDias", updatable = false)
  Integer frecuenciaEnDias;

  public Dinero(@NonNull Colaborador colaborador, Float monto, Integer frecuenciaEnDias) {
    super(colaborador);
    this.monto = monto;
    this.frecuenciaEnDias = frecuenciaEnDias;
  }

  protected Dinero() {}

  @Override
  public String toString() {
    return "Dinero{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        ", monto=" + monto +
        '}';
  }
}
