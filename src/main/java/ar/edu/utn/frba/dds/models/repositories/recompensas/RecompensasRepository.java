package ar.edu.utn.frba.dds.models.repositories.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.stream.Stream;

public class RecompensasRepository extends HibernateEntityManager<Recompensa, Long> {
  public Stream<Recompensa> findAll(Colaborador colaborador) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Recompensa> query = cb.createQuery(Recompensa.class);
    Root<Recompensa> recompensa = query.from(Recompensa.class);

    query.select(recompensa)
        .where(cb.equal(recompensa.get("proveedor"), colaborador));

    return em.createQuery(query).getResultStream();
  }

  public Stream<Recompensa> findAllConStock() {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Recompensa> query = cb.createQuery(Recompensa.class);
    Root<Recompensa> recompensa = query.from(Recompensa.class);

    Subquery<Long> canjeoSubquery = query.subquery(Long.class);
    Root<Canjeo> canjeosRoot = canjeoSubquery.from(Canjeo.class);
    canjeoSubquery.select(cb.count(canjeosRoot))
        .where(cb.equal(canjeosRoot.get("recompensa"), recompensa));

    query.select(recompensa)
        .where(cb.greaterThan(recompensa.get("stockInicial"), canjeoSubquery));

    return em.createQuery(query).getResultStream();
  }

  public Long findStock(Recompensa recompensa) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    Root<Canjeo> canjeoRoot = query.from(Canjeo.class);

    query.select(cb.count(canjeoRoot))
        .where(cb.equal(canjeoRoot.get("recompensa"), recompensa));

    Long cantidadCanjeada = em.createQuery(query).getSingleResult();
    return recompensa.getStockInicial() - cantidadCanjeada;
  }
}
