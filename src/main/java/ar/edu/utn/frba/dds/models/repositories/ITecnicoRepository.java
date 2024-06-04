package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;

import java.util.List;
import java.util.Optional;

public interface ITecnicoRepository {
  Optional<Tecnico> getTecnico(String cuil);

  List<Tecnico> getTecnicos();

  void insertTecnico(Tecnico tecnico);

  boolean deleteTecnico(String cuil);

  boolean updateTecnico(Tecnico tecnico);
}
