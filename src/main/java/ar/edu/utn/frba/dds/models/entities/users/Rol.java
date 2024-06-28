package ar.edu.utn.frba.dds.models.entities.users;

import lombok.NonNull;

import java.util.Set;

public record Rol(@NonNull String nombre, @NonNull Set<Permiso> permisos) {
  public boolean tienePermiso(@NonNull Permiso permiso) {
    return permisos.contains(permiso);
  }
}
