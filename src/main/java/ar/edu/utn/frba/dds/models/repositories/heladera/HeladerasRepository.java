package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeladerasRepository {
  private final List<Heladera> heladeras;

  public HeladerasRepository() {
    heladeras = new ArrayList<>();
  }

  public Optional<Heladera> get(int id) {
    return heladeras.stream().filter(heladera -> heladera.getId() == id).findFirst();
  }

  public Optional<Heladera> get(Ubicacion ubicacion) {
    return heladeras.stream().filter(heladera -> heladera.getUbicacion() == ubicacion).findFirst();
  }

  public int insert(Heladera heladera) throws RepositoryInsertException {
    if (get(heladera.getUbicacion()).isPresent()) {
      throw new RepositoryInsertException("Una heladera ya se encuentra en esa ubicación");
    }

    heladeras.add(heladera);
    heladera.setId(heladeras.size());

    return heladera.getId();
  }
}
