package ar.edu.utn.frba.dds.models.entities.documentacion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;

public class Tarjeta {
  @Getter
  final @NonNull UUID id;
  final @NonNull Colaborador proveedor;
  @Getter
  final @NonNull Usuario recipiente;

  public Tarjeta(@NonNull UUID id, @NonNull Colaborador proveedor, @NonNull Usuario recipiente) {
    this.id = id;
    this.proveedor = proveedor;
    this.recipiente = recipiente;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tarjeta that = (Tarjeta) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
