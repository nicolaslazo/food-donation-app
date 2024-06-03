package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.personaVulnerable.PersonaVulnerable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonaVulnerableRepository implements IPersonaVulnerableRepository {
  private final List<PersonaVulnerable> personasVulnerables;

  public PersonaVulnerableRepository() {
    personasVulnerables = new ArrayList<>();
  }

  public Optional<PersonaVulnerable> get(Documento documento) {
    return personasVulnerables
        .stream()
        .filter(personaVulnerable -> personaVulnerable.getDocumento() == documento)
        .findFirst();
  }
}