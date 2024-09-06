package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DineroRepository extends HibernateEntityManager<Dinero, Long> {
  public double getTotal(Colaborador colaborador) {
    String query = "SELECT SUM(d.monto) FROM Dinero d WHERE d.colaborador = :colaborador";
    return entityManager()
            .createQuery(query, Double.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult();
  }
}
