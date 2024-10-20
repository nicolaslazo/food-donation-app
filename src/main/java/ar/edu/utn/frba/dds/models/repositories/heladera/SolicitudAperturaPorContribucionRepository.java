package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
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
    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Long> query = cb.createQuery(Long.class);

      // Definimos un root, aunque no lo usemos directamente, para evitar el error
      Root<Heladera> heladeraRoot = query.from(Heladera.class);

      // Subconsulta para donaciones pendientes
      Subquery<Long> donacionesSubquery = query.subquery(Long.class);
      Root<DonacionViandas> donacionRoot = donacionesSubquery.from(DonacionViandas.class);

      // Convertimos el resultado de cb.size() a Long
      Expression<Long> sizeExpr = cb.toLong(cb.size(donacionRoot.get("viandas")));
      donacionesSubquery.select(cb.sum(sizeExpr))
              .where(cb.and(
                      cb.equal(donacionRoot.get("destino"), heladera),
                      cb.isNull(donacionRoot.get("fechaRealizada"))
              ));

      // Subconsulta para redistribuciones pendientes
      Subquery<Long> redistribucionesSubquery = query.subquery(Long.class);
      Root<RedistribucionViandas> redistribucionRoot = redistribucionesSubquery.from(RedistribucionViandas.class);

      // Convertimos el resultado de cb.size() a Long
      sizeExpr = cb.toLong(cb.size(redistribucionRoot.get("viandas")));
      redistribucionesSubquery.select(cb.sum(sizeExpr))
              .where(cb.and(
                      cb.equal(redistribucionRoot.get("destino"), heladera),
                      cb.isNull(redistribucionRoot.get("fechaRealizada"))
              ));

      // Consulta final sumando ambas subconsultas
      query.select(cb.sum(cb.coalesce(donacionesSubquery, 0L), cb.coalesce(redistribucionesSubquery, 0L)));

      // Ejecutar la consulta
      Long result = em.createQuery(query).getSingleResult();
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
