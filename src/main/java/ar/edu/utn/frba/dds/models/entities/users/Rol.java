package ar.edu.utn.frba.dds.models.entities.users;

import java.util.Set;

public class Rol {
  private String nombre;
  private Set<Permiso> permisos;

  public boolean tenesPermiso(Permiso permiso) {
    return permisos.contains(permiso);
  }
}
