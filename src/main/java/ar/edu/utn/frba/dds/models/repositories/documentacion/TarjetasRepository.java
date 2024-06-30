package ar.edu.utn.frba.dds.models.repositories.documentacion;


import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TarjetasRepository implements ITarjetasRepository {
  // TODO: testear
  private final List<Tarjeta> tarjetas;

  public TarjetasRepository() {
    tarjetas = new ArrayList<>();
  }

  public Optional<Tarjeta> get(UUID id) {
    return tarjetas.stream().filter(ta -> ta.getId().equals(id)).findFirst();
  }

  public void insert(Tarjeta tarjeta) throws RepositoryException {
    if (get(tarjeta.getId()).isPresent()) {
      throw new RepositoryException("Otra tarjeta alimentaria con este identificador ya existe");
    }

    tarjetas.add(tarjeta);
  }
}
