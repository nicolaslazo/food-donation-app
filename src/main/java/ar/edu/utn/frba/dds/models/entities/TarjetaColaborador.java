package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;

import lombok.NonNull;
import java.util.regex.Pattern;

import java.util.Objects;

public class TarjetaColaborador {
  private static final String REGEX_IDENTIFICADOR_TARJETA =
      "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
  private final @NonNull String identificador;
  private final @NonNull Colaborador recipiente;

  public TarjetaColaborador(@NonNull String identificador, @NonNull Colaborador recipiente) throws Exception
  {if (!Pattern.matches(REGEX_IDENTIFICADOR_TARJETA, identificador)) {
    throw new Exception("El identificador proveído no es válido");}
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
