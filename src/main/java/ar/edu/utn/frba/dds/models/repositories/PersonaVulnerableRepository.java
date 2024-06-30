package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonaVulnerableRepository implements IPersonaVulnerableRepository {
  private final List<PersonaVulnerable> personasVulnerables;

  private PersonaVulnerableRepository() {
    personasVulnerables = new ArrayList<>();
  }

  public Optional<PersonaVulnerable> get(Documento documento) {
    return personasVulnerables
        .stream()
        .filter(personaVulnerable -> personaVulnerable.getDocumento() == documento)
        .findFirst();
  }

  public void insert(PersonaVulnerable personaVulnerable) throws RepositoryException {
    if (get(personaVulnerable.getDocumento()).isPresent()) {
      throw new RepositoryException("Este documento ya está siendo usado");
    }
    personasVulnerables.add(personaVulnerable);
  }
}