package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.personaVulnerable.PersonaVulnerable;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

public final class TarjetaAlimentaria {
  @Getter
  private final @NonNull String identificador;
  @Getter
  private final @NonNull PersonaVulnerable recipiente;
  private final @NonNull Colaborador proveedor;

  public TarjetaAlimentaria(@NonNull String identificador, @NonNull PersonaVulnerable recipiente,
                            @NonNull Colaborador proveedor) {
    this.identificador = identificador;
    this.recipiente = recipiente;
    this.proveedor = proveedor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TarjetaAlimentaria that = (TarjetaAlimentaria) o;
    return Objects.equals(identificador, that.identificador);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificador);
  }
}
