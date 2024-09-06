package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuidadoHeladerasRepository extends HibernateEntityManager<CuidadoHeladera, Long> {
  public int getMesesActivosCumulativos(Colaborador colaborador) {
    /* Hago la diferencia entre los meses y años, multiplico el año por 12 para tener los meses */
    String jpql = "SELECT SUM(MONTH(CURRENT_DATE) - MONTH(ch.heladera.fechaInstalacion) " +
            "+ 12 * (YEAR(CURRENT_DATE) - YEAR(ch.heladera.fechaInstalacion))) " +
            "FROM CuidadoHeladera ch " +
            "WHERE ch.colaborador = :colaborador";

    return entityManager()
            .createQuery(jpql, Long.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult()
            .intValue();
  }
}
