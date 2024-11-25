package ar.edu.utn.frba.dds.models.repositories.documentacion;

import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.UUID;

public class TarjetasRepository extends HibernateEntityManager<Tarjeta, UUID> {
  public Optional<Tarjeta> getVigentePara(Usuario usuario) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Tarjeta> query = cb.createQuery(Tarjeta.class);
    Root<Tarjeta> root = query.from(Tarjeta.class);

    Predicate recipientePredicate = cb.equal(root.get("recipiente"), usuario);
    Predicate fechaAltaNotNullPredicate = cb.isNotNull(root.get("fechaAlta"));
    Predicate fechaBajaNullPredicate = cb.isNull(root.get("fechaBaja"));

    query.select(root)
        .where(cb.and(recipientePredicate, fechaAltaNotNullPredicate, fechaBajaNullPredicate));

    return em.createQuery(query).setMaxResults(1).getResultStream().findFirst();
  }

  public Optional<Tarjeta> findActivaByRecipiente(Usuario usuario) {
      EntityManager em = entityManager();
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Tarjeta> query = cb.createQuery(Tarjeta.class);
      Root<Tarjeta> root = query.from(Tarjeta.class);

      Predicate recipientePredicate = cb.equal(root.get("recipiente"), usuario);
      Predicate fechaAltaNotNullPredicate = cb.isNotNull(root.get("fechaAlta"));
      Predicate fechaBajaNullPredicate = cb.isNull(root.get("fechaBaja"));

      query.select(root)
          .where(cb.and(recipientePredicate, fechaAltaNotNullPredicate, fechaBajaNullPredicate));

      return em.createQuery(query).setMaxResults(1).getResultStream().findFirst();
  }
}
