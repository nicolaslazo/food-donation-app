package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViandasRepository {
  static ViandasRepository instancia = null;
  final List<Vianda> viandas;

  private ViandasRepository() {
    viandas = new ArrayList<>();
  }

  public static ViandasRepository getInstancia() {
    if (instancia == null) instancia = new ViandasRepository();

    return instancia;
  }

  public Optional<Vianda> get(int id) {
    return viandas.stream().filter(vianda -> vianda.getId() == id).findFirst();
  }

  public List<Vianda> getAlmacenadas(Heladera heladera) {
    return viandas.stream().filter(vianda -> vianda.getHeladera().getId() == heladera.getId()).toList();
  }

  public int insert(Vianda vianda) {
    // TODO: Asegurarnos que la heladera tiene espacio para esta vianda

    viandas.add(vianda);
    vianda.setId(viandas.size());

    return vianda.getId();
  }

  public void updateUbicacion(Vianda vianda, Heladera ubicacionNueva) {
    // TODO: Asegurarnos que la heladera tiene espacio para esta vianda

    vianda.setHeladera(ubicacionNueva);
  }

  public void deleteTodas() {
    viandas.clear();
  }
}
