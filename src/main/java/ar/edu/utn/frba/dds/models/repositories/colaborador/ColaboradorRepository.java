package ar.edu.utn.frba.dds.models.repositories.colaborador;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ColaboradorRepository {
  static ColaboradorRepository instancia = null;
  List<Colaborador> colaboradores;

  private ColaboradorRepository() {
    colaboradores = new ArrayList<>();
  }

  public static ColaboradorRepository getInstancia() {
    if (instancia == null) instancia = new ColaboradorRepository();

    return instancia;
  }

  public Optional<Colaborador> get(UUID id) {
    return colaboradores.stream().filter(colaborador -> colaborador.getId().equals(id)).findFirst();
  }

  public UUID insert(Colaborador colaborador) throws RepositoryException {
    if (colaboradores.contains(colaborador)) throw new RepositoryException("Este colaborador ya existe");

    colaboradores.add(colaborador);

    return colaborador.getId();
  }

  public void deleteTodos() {
    colaboradores.clear();
  }
}
