package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class PermisosRepository extends HibernateEntityManager<Permiso> {
  static PermisosRepository instancia = null;

  public PermisosRepository getInstancia() {
    if (instancia == null) instancia = new PermisosRepository();

    return instancia;
  }

  public Optional<Permiso> get(String nombre) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Permiso> query = cb.createQuery(Permiso.class);
    Root<Permiso> root = query.from(Permiso.class);

    query.select(root).where(cb.equal(root.get("nombre"), nombre));

    return em.createQuery(query).getResultStream().findFirst();
  }
}
