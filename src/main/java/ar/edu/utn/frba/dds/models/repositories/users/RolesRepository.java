package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class RolesRepository extends HibernateEntityManager<Rol, Long> {
  public Optional<Rol> findByName(String name) {
    CriteriaBuilder cb = entityManager().getCriteriaBuilder();
    CriteriaQuery<Rol> query = cb.createQuery(Rol.class);
    Root<Rol> rol = query.from(Rol.class);

    query.select(rol).where(cb.equal(rol.get("nombre"), name));

    return entityManager()
            .createQuery(query)
            .getResultStream()
            .findFirst();
  }
}