package ar.edu.utn.frba.dds.models.entities.users;

import lombok.NonNull;

import java.util.Objects;

public record Permiso(@NonNull String nombre, String descripcion) {
  public Permiso(String nombre) {
    this(nombre, null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permiso permiso = (Permiso) o;
    return Objects.equals(nombre, permiso.nombre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre);
  }
}
