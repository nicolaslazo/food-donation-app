package ar.edu.utn.frba.dds.models.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.stream.Stream;

public abstract class HibernateEntityManager<T> {
  protected EntityManager entityManager;

  public HibernateEntityManager() {
    EntityManagerFactory entityManagerFactory =
        Persistence.createEntityManagerFactory("simple-persistence-unit");

    entityManager = entityManagerFactory.createEntityManager();
  }

  public HibernateEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Stream<?> correrQuery(String query) {
    return entityManager.createQuery(query).getResultStream();
  }

  public void delete(T object) {
    empezarTransaccion();
    entityManager.remove(object);
    committearTransaccion();
  }

  public void insert(T object) {
    empezarTransaccion();
    entityManager.persist(object);
    committearTransaccion();
  }

  public void empezarTransaccion() {
    entityManager.getTransaction().begin();
  }

  public void rollbackearTransaccion() {
    entityManager.getTransaction().rollback();
  }

  public void committearTransaccion() {
    entityManager.getTransaction().commit();
  }

}
