package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;

import java.util.Optional;

public interface IPermisosRepository {
  Optional<Permiso> buscarPermisoPorNombre(String nombre);
}
