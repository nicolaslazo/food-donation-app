package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class VisitasTecnicasRepository extends HibernateEntityManager<VisitaTecnica, Long> {
  public boolean isIncidenteResuelto(Incidente incidente) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    Root<VisitaTecnica> root = query.from(VisitaTecnica.class);

    query.select(cb.count(root))
        .where(
            cb.equal(root.get("incidente"), incidente),
            cb.isTrue(root.get("incidenteResuelto"))
        );

    Long count = em.createQuery(query).getSingleResult();
    return count > 0;
  }

}
