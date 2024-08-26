package ar.edu.utn.frba.dds.models.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public abstract class HibernateEntityManager<T> {
  static EntityManagerFactory entityManagerFactory =
      Persistence.createEntityManagerFactory("simple-persistence-unit");
  Class<T> claseDeEntidad = getClaseDeEntidad();

  protected EntityManager instanciarEntityManager() {
    return entityManagerFactory.createEntityManager();
  }

  private Class<T> getClaseDeEntidad() {
    Type tipo = getClass().getGenericSuperclass();

    if (tipo instanceof ParameterizedType tipoDeEntidad) {
      return (Class<T>) tipoDeEntidad.getActualTypeArguments()[0];
    }
    throw new IllegalStateException("Esta clase no tiene parámetros genéricos");
  }

  public Stream<?> correrQuery(String query) {
    // CUIDADO: este método nos puede exponer a inyecciones SQL
    return instanciarEntityManager().createQuery(query).getResultStream();
  }

  public Stream<T> getAll() {
    return instanciarEntityManager()
        .createQuery("SELECT e FROM " + claseDeEntidad.getSimpleName() + " e", claseDeEntidad)
        .getResultStream();
  }

  public void insert(T object) {
    EntityManager em = instanciarEntityManager();

    empezarTransaccion(em);
    em.persist(object);
    committearTransaccion(em);
  }

  public void delete(T object) {
    EntityManager em = instanciarEntityManager();

    empezarTransaccion(em);
    em.remove(object);
    committearTransaccion(em);
  }

  public void deleteAll() {
    EntityManager em = instanciarEntityManager();

    empezarTransaccion(em);

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaDelete<T> delete = cb.createCriteriaDelete(getClaseDeEntidad());
    delete.from(claseDeEntidad);
    em.createQuery(delete).executeUpdate();

    committearTransaccion(em);
  }

  public void empezarTransaccion(EntityManager entityManager) {
    entityManager.getTransaction().begin();
  }

  public void rollbackearTransaccion(EntityManager entityManager) {
    entityManager.getTransaction().rollback();
  }

  public void committearTransaccion(EntityManager entityManager) {
    entityManager.getTransaction().commit();
  }
}
