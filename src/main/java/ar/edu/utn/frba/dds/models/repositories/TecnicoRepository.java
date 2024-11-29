package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

public class TecnicoRepository extends HibernateEntityManager<Tecnico, Long> {

  public Stream<AreaGeografica> findAllAreas() {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<AreaGeografica> query = cb.createQuery(AreaGeografica.class);
    Root<Tecnico> root = query.from(Tecnico.class);
    query.select(cb.construct(AreaGeografica.class,
        root.get("areaAsignada").get("centro"),
        root.get("areaAsignada").get("radioEnMetros")));
    return em.createQuery(query).getResultStream();
  }
}
