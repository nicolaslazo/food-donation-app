package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RedistribucionViandasRepository {
  static RedistribucionViandasRepository instancia = null;
  final List<RedistribucionViandas> redistribuciones;

  public RedistribucionViandasRepository() {
    this.redistribuciones = new ArrayList<>();
  }

  public static RedistribucionViandasRepository getInstancia() {
    if (instancia == null) {
      instancia = new RedistribucionViandasRepository();
    }

    return instancia;
  }

  public Optional<RedistribucionViandas> get(Long id) {
    return redistribuciones.stream().filter(redistribucion -> redistribucion.getId() == id).findFirst();
  }

  public int getTotal(Colaborador colaborador) {
    return redistribuciones
        .stream()
        .filter(redistribucion -> redistribucion.getColaborador() == colaborador)
        .mapToInt(RedistribucionViandas::getNumeroViandas)
        .sum();
  }

  public Long insert(RedistribucionViandas redistribucion) {
    redistribuciones.add(redistribucion);
    redistribucion.setId((long) redistribuciones.size());

    return redistribucion.getId();
  }

  public void deleteTodas() {
    redistribuciones.clear();
  }
}
