package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.util.Optional;

public class RolesRepository extends HibernateEntityManager<Rol, Long> {
  public Optional<Rol> findByName(String name) {
    return entityManager()
            .createQuery("from " + Rol.class.getName() + " where nombre =:name", Rol.class)
            .setParameter("name", name)
            .getResultStream()
            .findFirst();
  }
}
