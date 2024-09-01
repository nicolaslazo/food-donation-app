package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SuscripcionRepository {
  static SuscripcionRepository instancia = null;
  static HeladerasRepository heladerasRepository = new HeladerasRepository();
  List<Suscripcion> suscripciones;

  private SuscripcionRepository() {
    suscripciones = new ArrayList<>();
  }

  public static SuscripcionRepository getInstancia() {
    if (instancia == null) {
      instancia = new SuscripcionRepository();
    }

    return instancia;
  }

  public Optional<Suscripcion> get(int id) {
    return suscripciones.stream().filter(suscripcion -> suscripcion.getId() == id).findFirst();
  }

  public Optional<Suscripcion> get(Heladera heladera, MotivoDeDistribucion tipo, Colaborador colaborador) {
    return suscripciones
        .stream()
        .filter(suscripcion ->
            suscripcion.getHeladera() == heladera &&
                suscripcion.getTipo() == tipo &&
                suscripcion.getColaborador() == colaborador
        ).findFirst();
  }

  /* Dada una heladera, busca todas las suscripciones que deberían recibir una notificación relacionada a stock */
  public Stream<Suscripcion> getInteresadasEnStock(Heladera heladera) {
    int capacidadTotal = heladera.getCapacidadEnViandas();
    int cantidadViandasDepositadas = heladerasRepository.getCantidadViandasDepositadas(heladera);

    Stream<Suscripcion> porPocasViandas = suscripciones
        .stream()
        .filter(suscripcion ->
            suscripcion.getHeladera() == heladera &&
                suscripcion.getTipo() == MotivoDeDistribucion.FALTAN_VIANDAS &&
                cantidadViandasDepositadas < suscripcion.getParametro());

    Stream<Suscripcion> porPocoEspacio = suscripciones
        .stream()
        .filter(suscripcion ->
            suscripcion.getHeladera() == heladera &&
                suscripcion.getTipo() == MotivoDeDistribucion.FALTA_ESPACIO &&
                capacidadTotal - cantidadViandasDepositadas < suscripcion.getParametro());

    return Stream.concat(porPocasViandas, porPocoEspacio);
  }

  public Stream<Suscripcion> getInteresadasEnStock(Heladera heladera,
                                                   HeladerasRepository repository) {
    int capacidadTotal = heladera.getCapacidadEnViandas();
    int cantidadViandasDepositadas = repository.getCantidadViandasDepositadas(heladera);

    Stream<Suscripcion> porPocasViandas = suscripciones
            .stream()
            .filter(suscripcion ->
                    suscripcion.getHeladera() == heladera &&
                            suscripcion.getTipo() == MotivoDeDistribucion.FALTAN_VIANDAS &&
                            cantidadViandasDepositadas < suscripcion.getParametro());

    Stream<Suscripcion> porPocoEspacio = suscripciones
            .stream()
            .filter(suscripcion ->
                    suscripcion.getHeladera() == heladera &&
                            suscripcion.getTipo() == MotivoDeDistribucion.FALTA_ESPACIO &&
                            capacidadTotal - cantidadViandasDepositadas < suscripcion.getParametro());

    return Stream.concat(porPocasViandas, porPocoEspacio);
  }

  public Stream<Suscripcion> getTodas(Heladera heladera, MotivoDeDistribucion tipo) {
    return suscripciones
        .stream()
        .filter(suscripcion -> suscripcion.getHeladera() == heladera && suscripcion.getTipo() == tipo);
  }

  public int insert(Suscripcion suscripcion) throws RepositoryException {
    if (get(suscripcion.getHeladera(), suscripcion.getTipo(), suscripcion.getColaborador()).isPresent()) {
      throw new RepositoryException("Ese colaborador ya está inscrito a esa heladera para eso");
    }

    suscripciones.add(suscripcion);
    suscripcion.setId(suscripciones.size());

    return suscripcion.getId();
  }

  public void deleteTodas() {
    suscripciones.clear();
  }
}
