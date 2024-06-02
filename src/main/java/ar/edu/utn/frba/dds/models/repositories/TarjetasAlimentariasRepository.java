package ar.edu.utn.frba.dds.models.repositories;


import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarjetasAlimentariasRepository implements ITarjetasAlimentariasRepository {
  // TODO: testear
  private final List<TarjetaAlimentaria> tarjetasAlimentarias;

  public TarjetasAlimentariasRepository() {
    tarjetasAlimentarias = new ArrayList<>();
  }

  public Optional<TarjetaAlimentaria> get(String id) {
    return tarjetasAlimentarias
        .stream()
        .filter(ta -> ta.getIdentificador().equals(id))
        .findFirst();
  }

  public void insert(TarjetaAlimentaria tarjetaAlimentaria) throws RepositoryInsertException {
    if (get(tarjetaAlimentaria.getIdentificador()).isPresent()) {
      throw new RepositoryInsertException("Otra tarjeta alimentaria con este identificador ya existe");
    }

    tarjetasAlimentarias.add(tarjetaAlimentaria);
  }
}
