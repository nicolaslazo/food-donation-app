package ar.edu.utn.frba.dds.models.repositories.documentacion;

import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.Optional;
import java.util.UUID;

public interface ITarjetasRepository {
  void insert(Tarjeta tarjeta) throws RepositoryException;

  Optional<Tarjeta> get(UUID id);
}
