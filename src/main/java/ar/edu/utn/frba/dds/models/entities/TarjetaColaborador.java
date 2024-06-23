package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

public class TarjetaColaborador {
  @Getter
  private final @NonNull String identificador;
  @Getter
  private final @NonNull Colaborador recipiente;

  public TarjetaColaborador(@NonNull String identificador, @NonNull Colaborador recipiente) {
    this.identificador = identificador;
    this.recipiente = recipiente;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TarjetaColaborador that = (TarjetaColaborador) o;
    return Objects.equals(identificador, that.identificador);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificador);
  }
}
