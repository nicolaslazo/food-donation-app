package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeladerasRepository {
  static HeladerasRepository instancia = null;
  final List<Heladera> heladeras;

  public HeladerasRepository() {
    heladeras = new ArrayList<>();
  }
  // TODO: implementar deleteTodas

  public static HeladerasRepository getInstancia() {
    if (instancia == null) {
      instancia = new HeladerasRepository();
    }
    return instancia;
  }

  public Optional<Heladera> get(int id) {
    return heladeras.stream().filter(heladera -> heladera.getId() == id).findFirst();
  }

  public Optional<Heladera> get(Ubicacion ubicacion) {
    return heladeras.stream().filter(heladera -> heladera.getUbicacion() == ubicacion).findFirst();
  }

  public List<Heladera> getTodas(Colaborador colaborador) {
    return heladeras.stream().filter(heladera -> heladera.getEncargado() == colaborador).toList();
  }

  public int getMesesActivosCumulativos(Colaborador colaborador) {
    return getTodas(colaborador).stream().mapToInt(Heladera::mesesActiva).sum();
  }

  public int insert(Heladera heladera) throws RepositoryInsertException {
    if (get(heladera.getUbicacion()).isPresent()) {
      throw new RepositoryInsertException("Una heladera ya se encuentra en esa ubicación");
    }

    heladeras.add(heladera);
    heladera.setId(heladeras.size());

    return heladera.getId();
  }

  public void updateEstadoHeladera(int id, Heladera nuevaHeladera) {
    Optional<Heladera> heladera = get(id);
      heladera.ifPresent(value -> value.setHeladeraActiva(nuevaHeladera.isHeladeraActiva()));
  }

  public void updateTiempoHeladera(int id, Heladera nuevaHeladera) {
    Optional<Heladera> heladera = get(id);
    heladera.ifPresent(value -> value.setUltimaTempRegistradaCelsius(
            nuevaHeladera.getUltimaTempRegistradaCelsius())
    );
  }
}

