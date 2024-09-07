package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncidenteRepository extends HibernateEntityManager<Incidente, Long> {
  public Map<Heladera, Long> findCantidadIncidentesPorHeladeraSemanaPasada() {
    EntityManager em = entityManager();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = cb.createTupleQuery();
    Root<Incidente> root = query.from(Incidente.class);

    ZonedDateTime haceUnaSemana = ZonedDateTime.now().minusDays(7);

    Join<Incidente, Heladera> join = root.join("heladera");

    query.multiselect(
        join.alias("heladera"),
        cb.count(root).alias("cantidad")
    );

    query.where(cb.greaterThanOrEqualTo(root.get("fecha"), haceUnaSemana));
    query.groupBy(join);

    List<Tuple> results = em.createQuery(query).getResultList();

    Map<Heladera, Long> retval = new HashMap<>();
    for (Tuple tuple : results) {
      Heladera heladera = tuple.get("heladera", Heladera.class);
      Long count = tuple.get("cantidad", Long.class);
      retval.put(heladera, count);
    }

    return retval;
  }
}
