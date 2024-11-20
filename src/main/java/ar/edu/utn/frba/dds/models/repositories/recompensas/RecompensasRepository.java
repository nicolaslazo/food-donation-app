package ar.edu.utn.frba.dds.models.repositories.recompensas;

import java.util.List;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RecompensasRepository extends HibernateEntityManager<Recompensa, Long> {
  public List<Recompensa> findAll(Colaborador colaborador) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Recompensa> query = cb.createQuery(Recompensa.class);
    Root<Recompensa> recompensa = query.from(Recompensa.class);

    query.select(recompensa)
        .where(cb.equal(recompensa.get("proveedor"), colaborador));

    return em.createQuery(query).getResultList();
  }
}
