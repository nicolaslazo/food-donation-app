package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

public class RedistribucionViandasRepository extends HibernateEntityManager<RedistribucionViandas, Long> {
  public int getTotal(Colaborador colaborador) {
    String query = "SELECT SUM(r.numeroViandas) FROM RedistribucionViandas r WHERE r.colaborador = :colaborador";
    Integer totalViandas = entityManager()
        .createQuery(query, Integer.class)
        .setParameter("colaborador", colaborador)
        .getSingleResult();

    // Si el resultado es null (es decir, no hay redistribuciones), retornamos 0
    return totalViandas != null ? totalViandas : 0;
  }

}
