package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;

import java.util.Optional;

public interface ITarjetasAlimentariasRepository {
  void insert(TarjetaAlimentaria tarjetaAlimentaria) throws RepositoryInsertException;

  Optional<TarjetaAlimentaria> get(String id);
}
