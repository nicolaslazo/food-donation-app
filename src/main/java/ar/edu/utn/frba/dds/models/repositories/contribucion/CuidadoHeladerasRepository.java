package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

public class CuidadoHeladerasRepository extends HibernateEntityManager<CuidadoHeladera, Long> {
  public int getMesesActivosCumulativos(Colaborador colaborador) {
    /* Hago la diferencia entre los meses y años, multiplico el año por 12 para tener los meses */
    String jpql = """
            SELECT COALESCE(SUM(CASE\s
                WHEN ch.heladera.fechaInstalacion <= :currentDate\s
                THEN FLOOR(DATEDIFF(:currentDate, ch.heladera.fechaInstalacion) / 30.44)\s
                ELSE 0 END), 0)\s
            FROM CuidadoHeladera ch\s
            WHERE ch.colaborador = :colaborador""";

    return entityManager()
            .createQuery(jpql, Long.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult()
            .intValue();
  }
}
