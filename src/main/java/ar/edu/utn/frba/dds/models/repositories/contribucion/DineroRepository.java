package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

public class DineroRepository extends HibernateEntityManager<Dinero, Long> {
  public Double getTotal(Colaborador colaborador) {
    String query = "SELECT SUM(d.monto) FROM Dinero d WHERE d.colaborador = :colaborador";
    Double resultado = entityManager()
            .createQuery(query, Double.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult();          
    return resultado != null ? resultado : 0D;
  }
}
