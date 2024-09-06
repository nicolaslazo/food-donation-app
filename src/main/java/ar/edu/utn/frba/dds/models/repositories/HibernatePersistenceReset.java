package ar.edu.utn.frba.dds.models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;

public class HibernatePersistenceReset implements WithSimplePersistenceUnit {
  public void execute() {
    EntityManager em = entityManager();
    em.getTransaction().begin();
    em.createNativeQuery("TRUNCATE SCHEMA public AND COMMIT").executeUpdate();
    em.getTransaction().commit();
    em.close();
  }
}
