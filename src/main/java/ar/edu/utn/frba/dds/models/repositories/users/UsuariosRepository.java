package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.stream.Stream;


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

  public Stream<Usuario> findFraudulentosActivos() {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);

    Root<SolicitudAperturaPorContribucion> solicitud = query.from(SolicitudAperturaPorContribucion.class);
    Join<SolicitudAperturaPorContribucion, RedistribucionViandas> redistribucion = solicitud.join("razon");
    Join<RedistribucionViandas, Colaborador> colaborador = redistribucion.join("colaborador");
    Join<Colaborador, Usuario> usuario = colaborador.join("usuario");

    Predicate condiciones = cb.and(
        cb.isTrue(usuario.get("activo")),  // Sigue activo
        cb.isNotNull(solicitud.get("fechaAperturaEnOrigen")),  // Inició una redistribución
        cb.isNull(solicitud.get("fechaAperturaEnDestino")),  // No la terminó
        cb.greaterThan(cb.literal(ZonedDateTime.now()), solicitud.get("fechaVencimiento"))  // Pero ya venció
    );

    query.select(usuario)
        .where(condiciones)
        .distinct(true);

    return em.createQuery(query).getResultStream();
  }
}
