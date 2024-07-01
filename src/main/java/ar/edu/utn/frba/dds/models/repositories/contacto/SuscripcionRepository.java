package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contacto.TipoNotificacion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuscripcionRepository {
  static SuscripcionRepository instancia = null;
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

  public List<Suscripcion> get(Heladera heladera) {
    return suscripciones.stream().filter(suscripcion -> suscripcion.getHeladera() == heladera).toList();
  }

  public List<Suscripcion> get(Heladera heladera, TipoNotificacion tipoNotificacion) {
    return suscripciones
        .stream()
        .filter(suscripcion ->
            suscripcion.getHeladera() == heladera && suscripcion.getTipoNotificacion() == tipoNotificacion)
        .toList();
  }

  public List<Suscripcion> get(Heladera heladera, Colaborador colaborador) {
    return suscripciones
        .stream()
        .filter(suscripcion -> suscripcion.getHeladera() == heladera && suscripcion.getColaborador() == colaborador)
        .toList();
  }

  public Optional<Suscripcion> get(Heladera heladera, TipoNotificacion tipoNotificacion, Colaborador colaborador) {
    return suscripciones
        .stream()
        .filter(suscripcion ->
            suscripcion.getHeladera() == heladera &&
                suscripcion.getTipoNotificacion() == tipoNotificacion &&
                suscripcion.getColaborador() == colaborador
        ).findFirst();
  }

  public int insert(Suscripcion suscripcion) throws RepositoryException {
    if (get(suscripcion.getHeladera(), suscripcion.getTipoNotificacion(), suscripcion.getColaborador()).isPresent()) {
      throw new RepositoryException("Ese colaborador ya está inscrito a esa heladera");
    }

    suscripciones.add(suscripcion);
    suscripcion.setId(suscripciones.size());

    return suscripcion.getId();
  }

  public void deleteTodas() {
    suscripciones.clear();
  }
}
