package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

  // TODO: ¿Esta obsoleto este método?
  public void updateUbicacion(Vianda vianda, Heladera ubicacionNueva) {
    vianda.setHeladera(ubicacionNueva);
    update(vianda);
  }
}
