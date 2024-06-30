package ar.edu.utn.frba.dds.models.entities.users;

import lombok.NonNull;

public record Permiso(@NonNull String nombre, String descripcion) {
  public Permiso(String nombre) {
    this(nombre, null);
  }
}
