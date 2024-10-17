package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SolicitudAperturaPorContribucionRepository extends HibernateEntityManager<SolicitudAperturaPorContribucion, Long> {

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigenteAlMomento(Long id,
                                                                                 boolean paraExtraccion,
                                                                                 ZonedDateTime momento) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<SolicitudAperturaPorContribucion> query = cb.createQuery(SolicitudAperturaPorContribucion.class);
    Root<SolicitudAperturaPorContribucion> root = query.from(SolicitudAperturaPorContribucion.class);
    query.select(root).where(cb.equal(root.get("id"), id));
    return em.createQuery(query)
            .getResultStream()
            .filter(solicitud -> solicitud.isVigenteAlMomento(momento, paraExtraccion))
            .findFirst();
  }

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigente(Long id, boolean paraExtraccion) {
    return getSolicitudVigenteAlMomento(id, paraExtraccion, ZonedDateTime.now());
  }

  public int getCantidadViandasPendientes(Heladera heladera) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<MovimientoViandas> query = cb.createQuery(MovimientoViandas.class);
    Root<MovimientoViandas> root = query.from(MovimientoViandas.class);
    query.select(root).where(cb.equal(root.get("destino").get("id"), heladera.getId()));
    return em.createQuery(query)
            .getResultStream()
            .mapToInt(solicitud -> solicitud.getViandas().size())
            .sum();
  }

  public void updateFechaUsada(Long id, boolean paraExtraccion, ZonedDateTime fechaUsada)
          throws Exception {
    Optional<SolicitudAperturaPorContribucion> optionalSolicitud =
            getSolicitudVigenteAlMomento(id, paraExtraccion, fechaUsada);

    if (optionalSolicitud.isEmpty()) {
      String operacion = paraExtraccion ? "extracción" : "depósito";

      throw new SolicitudInvalidaException(
              "No existe solicitud vigente con id %d para %s de viandas".formatted(id, operacion));
    }

    if (paraExtraccion) {
      optionalSolicitud.get().setFechaAperturaEnOrigen(fechaUsada);
    } else {
      optionalSolicitud.get().setFechaAperturaEnDestino(fechaUsada);
    }
    update(optionalSolicitud.get());
  }

}
