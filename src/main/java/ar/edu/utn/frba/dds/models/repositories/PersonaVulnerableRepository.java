package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonaVulnerableRepository {

  private final List<PersonaVulnerable> personasVulnerables;

  public PersonaVulnerableRepository() {
    personasVulnerables = new ArrayList<>();
  }

  public void insert(PersonaVulnerable personaVulnerable) {
    personasVulnerables.add(personaVulnerable);
  }

  public Optional<PersonaVulnerable> get(int id) {
    return personasVulnerables
        .stream()
        .filter(personaVulnerable -> personaVulnerable.getId() == id)
        .findFirst();
  }
}