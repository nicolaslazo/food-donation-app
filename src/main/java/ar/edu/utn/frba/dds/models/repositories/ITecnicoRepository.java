package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;

import java.util.List;
import java.util.Optional;

public interface ITecnicoRepository {
  Optional<Tecnico> get(String cuil);

  List<Tecnico> getTecnicos();

  void insert(Tecnico tecnico);

  boolean delete(String cuil);

}
