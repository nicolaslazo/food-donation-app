package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermisosRepository implements IPermisosRepository {
  static PermisosRepository instancia = null;
  final List<Permiso> permisos = new ArrayList<>();

  public PermisosRepository getInstancia() {
    if (instancia == null) instancia = new PermisosRepository();

    return instancia;
  }

  public Optional<Permiso> get(String nombre) {
    return permisos.stream().filter(permiso -> permiso.nombre().equalsIgnoreCase(nombre)).findFirst();
  }

  public void insert(Permiso permiso) throws RepositoryInsertException {
    if (get(permiso.nombre()).isPresent()) throw new RepositoryInsertException("Ya existe un permiso con ese nombre");

    permisos.add(permiso);
  }
}
