package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import lombok.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.stream.Stream;

public class ContactosRepository extends HibernateEntityManager<Contacto, Long> {
  public <T extends Contacto> Optional<T> get(String username, Class<T> tipo) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(tipo);
    Root<T> root = query.from(tipo);

    query.select(root).where(cb.equal(root.get("destinatario"), username));

    return em.createQuery(query).getResultStream().findFirst();
  }

  public Stream<Contacto> get(@NonNull Usuario usuario) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Contacto> query = cb.createQuery(Contacto.class);
    Root<Contacto> root = query.from(Contacto.class);

    query.select(root).where(cb.equal(root.get("usuario"), usuario));

    return em.createQuery(query).getResultStream();
  }

  public Stream<Contacto> get(@NonNull Colaborador colaborador) {
    return get(colaborador.getUsuario());
  }

  public Stream<Contacto> get(@NonNull Tecnico tecnico) {
    return get(tecnico);
  }

  public void updateChatId(String username, long chatId) throws RepositoryException {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Telegram> query = cb.createQuery(Telegram.class);
    Root<Telegram> root = query.from(Telegram.class);

    query.select(root).where(cb.equal(root.get("destinatario"), username));

    Telegram encontrado = em
        .createQuery(query)
        .getResultStream()
        .findFirst()
        .orElseThrow(() -> new RepositoryException("No existe un contacto con ese usuario"));

    encontrado.setChatId(chatId);
    update(encontrado);
  }
}
