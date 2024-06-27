package ar.edu.utn.frba.dds.models.repositories.documentacion;

import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;

import java.util.Optional;

public interface ITarjetasRepository {
  void insert(Tarjeta tarjeta) throws RepositoryInsertException;

  Optional<Tarjeta> get(String id);
}
