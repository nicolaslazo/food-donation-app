package ar.edu.utn.frba.dds.models.entities.documentacion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class Tarjeta {
  private static final String REGEX_IDENTIFICADOR_TARJETA =
      "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
  @Getter
  private final @NonNull String identificador;
  private final @NonNull Colaborador proveedor;
  private final @NonNull Usuario recipiente;

  public Tarjeta(@NonNull String identificador, @NonNull Colaborador proveedor, @NonNull Usuario recipiente) throws Exception {
    if (!Pattern.matches(REGEX_IDENTIFICADOR_TARJETA, identificador)) {
      throw new Exception("El identificador proveído no es válido");
    }

    this.identificador = identificador;
    this.proveedor = proveedor;
    this.recipiente = recipiente;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tarjeta that = (Tarjeta) o;
    return Objects.equals(identificador, that.identificador);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificador);
  }
}
