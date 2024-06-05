package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermisosRepository implements IPermisosRepository {
  private static List<Permiso> permisos = List.of();


  public PermisosRepository() {
    permisos = new ArrayList<>();
  }

  public Optional<Permiso> buscarPermisoPorNombre(String nombre) {
    return permisos
        .stream()
        .filter(permiso -> permiso.getNombrePermiso().equalsIgnoreCase(nombre))
        .findFirst();
  }
}
