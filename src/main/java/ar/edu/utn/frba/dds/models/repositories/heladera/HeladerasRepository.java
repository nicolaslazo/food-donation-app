package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeladerasRepository {
  static HeladerasRepository instancia = null;
  final List<Heladera> heladeras;

  private HeladerasRepository() {
    heladeras = new ArrayList<>();
  }

  public static HeladerasRepository getInstancia() {
    if (instancia == null) instancia = new HeladerasRepository();

    return instancia;
  }

  public Optional<Heladera> get(int id) {
    return heladeras.stream().filter(heladera -> heladera.getId() == id).findFirst();
  }

  public Optional<Heladera> get(Ubicacion ubicacion) {
    return heladeras.stream().filter(heladera -> heladera.getUbicacion() == ubicacion).findFirst();
  }

  public List<Heladera> getHeladerasConTemperaturaDesactualizada(int limiteEnMinutos) {
    return heladeras
        .stream()
        .filter(heladera -> heladera
            .getMomentoUltimaTempRegistrada()
            .isBefore(ZonedDateTime.now().minusMinutes(limiteEnMinutos)))
        .toList();
  }

  public List<Heladera> getTodas() {
    return heladeras;
  }

  public List<Heladera> getTodas(Colaborador encargado) {
    return heladeras.stream().filter(heladera -> heladera.getEncargado() == encargado).toList();
  }

  public int getMesesActivosCumulativos(Colaborador colaborador) {
    // TODO: Actualizar en base a la fecha del último incidente resuelto
    return getTodas(colaborador).stream().mapToInt(Heladera::mesesActiva).sum();
  }

  public int getCapacidadDisponible(Heladera heladera) {
    final int viandasActualmenteDepositadas = ViandasRepository.getInstancia().getAlmacenadas(heladera).size();
    final int viandasEnContribucionesVigentes =
        SolicitudAperturaPorContribucionRepository.getInstancia().getCantidadViandasPendientes(heladera);

    return heladera.getCapacidadEnViandas() - viandasActualmenteDepositadas - viandasEnContribucionesVigentes;
  }

  public int insert(Heladera heladera) throws RepositoryException {
    if (get(heladera.getUbicacion()).isPresent()) {
      throw new RepositoryException("Una heladera ya se encuentra en esa ubicación");
    }

    heladeras.add(heladera);
    heladera.setId(heladeras.size());

    return heladera.getId();
  }

  public void updateTiempoHeladera(int id, Heladera nuevaHeladera) {
    Optional<Heladera> heladera = get(id);
    heladera.ifPresent(value -> value.setUltimaTempRegistradaCelsius(
            nuevaHeladera.getUltimaTempRegistradaCelsius())
    );
  }

  public List<Heladera> getHeladerasConTemperaturaDesactualizada(int limiteEnMinutos) {
    return heladeras
            .stream()
            .filter(heladera -> heladera
                    .getMomentoUltimaTempRegistrada()
                    .isBefore(ZonedDateTime.now().minusMinutes(limiteEnMinutos)))
            .toList();
  }

  public void deleteTodas() {
    heladeras.clear();
  }
}

