package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.stream.Stream;

public class PermisosRepository extends HibernateEntityManager<Permiso, Long> {
  public Optional<Permiso> findByName(String nombre) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Permiso> query = cb.createQuery(Permiso.class);
    Root<Permiso> root = query.from(Permiso.class);

    query.select(root).where(cb.equal(root.get("nombre"), nombre));

    return em.createQuery(query).getResultStream().findFirst();
  }

  public Stream<Permiso> findAll(Usuario usuario) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Permiso> query = cb.createQuery(Permiso.class);
    Root<Usuario> rootUsuario = query.from(Usuario.class);

    Join<Usuario, Rol> roles = rootUsuario.join("roles");
    Join<Rol, Permiso> permisos = roles.join("permisos");

    query.select(permisos) .where(cb.equal(rootUsuario.get("id"), usuario.getId())) .distinct(true);

    return em.createQuery(query).getResultStream();
  }
}
