package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.Optional;

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
    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Long> query = cb.createQuery(Long.class);
      Root<MovimientoViandas> movimientoRoot = query.from(MovimientoViandas.class);

      // Expresión para calcular el total de viandas pendientes
      Expression<Long> sizeExpr = cb.toLong(cb.size(movimientoRoot.get("viandas")));
      query.select(cb.sum(sizeExpr))
          .where(cb.and(
              cb.equal(movimientoRoot.get("destino"), heladera),
              cb.isNull(movimientoRoot.get("fechaRealizada"))
          ));

      // Ejecutar la consulta
      Long result = em.createQuery(query).getSingleResult();
      em.close();
      return result != null ? result.intValue() : 0;

    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    } finally {
      if (em != null && em.isOpen()) {
        em.close();
      }
    }
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
