package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class UsuariosRepository extends HibernateEntityManager<Usuario, Long> {
  public Usuario findByEmailAndPassword(String email, String password) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
    Root<Usuario> root = query.from(Usuario.class);
    Join<Usuario, Contacto> contactoJoin = root.join("contactos");

    //Predicados para email y contraseña
    Predicate emailPredicate = cb.equal(contactoJoin.get("destinatario"), email);
    Predicate passwordPredicate = cb.equal(root.get("contraseniaHasheada"), password);

    //Preparo la query con los predicados
    query.select(root)
            .where(cb.and(emailPredicate, passwordPredicate));

    //Realizo la query
    return em.createQuery(query).getSingleResult();
  }

  public Usuario findByEmail(String email) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
    Root<Usuario> root = query.from(Usuario.class);
    Join<Usuario, Contacto> contactoJoin = root.join("contactos");

    query.select(root)
        .where(cb.equal(contactoJoin.get("destinatario"), email));

    try {
      return em.createQuery(query).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
