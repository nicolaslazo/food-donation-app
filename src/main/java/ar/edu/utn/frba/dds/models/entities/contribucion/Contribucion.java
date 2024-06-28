package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Getter
public abstract class Contribucion {
  final @NonNull Colaborador colaborador;
  ZonedDateTime fechaRealizada = null;

  public Contribucion(@NonNull Colaborador colaborador) {
    this.colaborador = colaborador;
  }

  public void setFechaRealizada(@NonNull ZonedDateTime fechaRealizada) throws ContribucionYaRealizadaException {
    if (this.fechaRealizada != null)
      throw new ContribucionYaRealizadaException("Esta contribución ya fue realizada en" + this.fechaRealizada);

    this.fechaRealizada = fechaRealizada;
  }

  @Override
  public String toString() {
    return "Contribucion{" +
        "colaborador=" + colaborador +
        ", fecha=" + fechaRealizada +
        '}';
  }
}