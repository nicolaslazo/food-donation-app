package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.util.List;

public class RedistribucionViandasRepository extends HibernateEntityManager<RedistribucionViandas, Long> {
  public int getTotal(Colaborador colaborador) {
    String query = "SELECT r FROM RedistribucionViandas r WHERE r.colaborador = :colaborador";
    List<RedistribucionViandas> redistribuciones = entityManager()
        .createQuery(query, RedistribucionViandas.class)
        .setParameter("colaborador", colaborador)
        .getResultList();

    // Sumamos la cantidad de viandas redistribuidas
    return redistribuciones.stream()
        .mapToInt(RedistribucionViandas::getNumeroViandas)
        .sum();
  }
}
