package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitasTecnicasRepository extends HibernateEntityManager<VisitaTecnica, Long> {
  public boolean isIncidenteResuelto(Incidente incidente) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    Root<VisitaTecnica> root = query.from(VisitaTecnica.class);

    // Contamos el número de visitas que tienen el incidente y están resueltas
    query.select(cb.count(root))
        .where(
            cb.equal(root.get("incidente"), incidente),
            cb.isTrue(root.get("incidenteResuelto"))
        );

    Long count = em.createQuery(query).getSingleResult();
    return count > 0;
  }

}
