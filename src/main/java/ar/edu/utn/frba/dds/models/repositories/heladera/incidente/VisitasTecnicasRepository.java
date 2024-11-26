package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

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

  // Retorna todos los reportes que pertecen a heladeras que tiene a cargo el colaborador
  public Stream<VisitaTecnica> findAllVisitasTecnicas(Colaborador colaborador) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<VisitaTecnica> query = cb.createQuery(VisitaTecnica.class);
    Root<VisitaTecnica> visitaRoot = query.from(VisitaTecnica.class);

    // Joins necesarios para llegar al colaborador a través de heladera
    Join<VisitaTecnica, Incidente> incidenteJoin = visitaRoot.join("incidente");
    Join<Incidente, Heladera> heladeraJoin = incidenteJoin.join("heladera");

    // Condición: La heladera debe estar asociada al colaborador dado
    Predicate perteneceAlColaborador = cb.equal(heladeraJoin.get("encargado"), colaborador);

    query.select(visitaRoot).where(perteneceAlColaborador);

    // Ejecutamos la query
    return em.createQuery(query).getResultStream();
  }

}
