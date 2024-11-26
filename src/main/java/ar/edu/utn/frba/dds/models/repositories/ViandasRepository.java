package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class ViandasRepository extends HibernateEntityManager<Vianda, Long> {
  public Stream<Vianda> findAll(Heladera heladera) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Vianda> query = cb.createQuery(Vianda.class);
    Root<Vianda> root = query.from(Vianda.class);

    query.select(root).where(cb.equal(root.get("heladera"), heladera));

    return em.createQuery(query).getResultStream();
  }

  public Stream<Vianda> findAllViandasDisponiblesARetirar() {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Vianda> query = cb.createQuery(Vianda.class);
    Root<Vianda> root = query.from(Vianda.class);

    // Subquery para encontrar viandas con solicitudes vigentes
    Subquery<Long> subquery = query.subquery(Long.class);
    Root<SolicitudAperturaPorConsumicion> solicitudRoot = subquery.from(SolicitudAperturaPorConsumicion.class);

    // Subquery selecciona las viandas con solicitudes vigentes
    subquery.select(solicitudRoot.get("vianda").get("id"))
            .where(cb.and(
                    cb.equal(solicitudRoot.get("vianda"), root),
                    cb.isNull(solicitudRoot.get("fechaUsada")),
                    cb.greaterThanOrEqualTo(solicitudRoot.get("fechaVencimiento"), ZonedDateTime.now()),
                    cb.lessThanOrEqualTo(solicitudRoot.get("fechaCreacion"), ZonedDateTime.now())
            ));

    // Query principal: busca viandas que no estén en el subquery y cuya heladera no sea null
    query.select(root)
            .where(cb.and(
                    cb.isNotNull(root.get("heladera")),
                    cb.not(cb.exists(subquery))
            ));

    return em.createQuery(query).getResultStream();
  }

}
