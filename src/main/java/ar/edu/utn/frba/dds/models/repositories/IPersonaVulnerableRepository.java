package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;

import java.util.Optional;
import java.util.UUID;

public interface IPersonaVulnerableRepository {
  void insert(PersonaVulnerable personaVulnerable) throws RepositoryException;

  Optional<PersonaVulnerable> get(UUID id);
}
