package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonaVulnerableRepository implements IPersonaVulnerableRepository {
  private final List<PersonaVulnerable> personasVulnerables;

  private PersonaVulnerableRepository() {
    personasVulnerables = new ArrayList<>();
  }

  public Optional<PersonaVulnerable> get(UUID uuid) {
    return personasVulnerables.stream().filter(personaVulnerable -> personaVulnerable.getId() == uuid).findFirst();
  }

  public void insert(PersonaVulnerable personaVulnerable) throws RepositoryException {
    if (get(personaVulnerable.getId()).isPresent()) {
      throw new RepositoryException("Este documento ya está siendo usado");
    }

    personasVulnerables.add(personaVulnerable);
  }
}