package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.domain.recompensas.Recompensa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecompensasRepository {
  private List<Recompensa> recompensas;

  public RecompensasRepository() {
    recompensas = new ArrayList<>();
  }
  public Optional<Recompensa> get(int id) {
    return recompensas
        .stream()
        .filter(recompensa -> recompensa.getId() == id)
        .findFirst();
  }

  public List<Recompensa> getTodos() {
    return recompensas;
  }

  public void insert(Recompensa recompensa) {
    recompensa.setId(recompensas.size() + 1);
    recompensas.add(recompensa);
  }

  public void update(Recompensa categoria) {
    //DO NOTHING
  }
}
