package ar.edu.utn.frba.dds.models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/* T es el tipo de entidad, U el tipo de pkey que usa */
public abstract class HibernateEntityManager<T, U> implements WithSimplePersistenceUnit {
  Class<T> claseDeEntidad = getClaseDeEntidad();

  private Class<T> getClaseDeEntidad() {
    Type tipo = getClass().getGenericSuperclass();

    if (tipo instanceof ParameterizedType tipoDeEntidad) //noinspection unchecked
      return (Class<T>) tipoDeEntidad.getActualTypeArguments()[0];
    throw new IllegalStateException("Esta clase no tiene parámetros genéricos");
  }

  public Stream<?> correrQuery(String query) {
    // CUIDADO: este método nos puede exponer a inyecciones SQL
    //noinspection SqlSourceToSinkFlow
    return entityManager().createQuery(query).getResultStream();
  }

  public Optional<T> findById(U id) {
    return Optional.ofNullable(entityManager().find(claseDeEntidad, id));
  }

  public Stream<T> findAll() {
    return entityManager()
        .createQuery("SELECT e FROM " + claseDeEntidad.getSimpleName() + " e", claseDeEntidad)
        .getResultStream();
  }

  public void insert(T object) {
    withTransaction(() -> entityManager().persist(object));
  }

  public void insertAll(Collection<T> objects) {
    withTransaction(() -> {
      for (T object : objects) entityManager().persist(object);
    });
  }

  public void update(T object) {
    withTransaction(() -> entityManager().merge(object));
  }

  public void delete(T object) {
    withTransaction(() -> entityManager().remove(object));
  }

  public void deleteAll() {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaDelete<T> delete = cb.createCriteriaDelete(getClaseDeEntidad());

    withTransaction(() -> {
      delete.from(claseDeEntidad);
      em.createQuery(delete).executeUpdate();
    });
  }

  public void clearCache() {
    EntityManager em = entityManager();
    em.clear();
  }
}
