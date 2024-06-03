package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.personaVulnerable.PersonaVulnerable;

import java.util.Optional;

public interface IPersonaVulnerableRepository {
  public void insert(PersonaVulnerable personaVulnerable) throws RepositoryInsertException;

  public Optional<PersonaVulnerable> get(Documento documento);
}
