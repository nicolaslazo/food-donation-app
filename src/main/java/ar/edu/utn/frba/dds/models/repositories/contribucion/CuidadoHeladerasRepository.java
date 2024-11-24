package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.time.ZonedDateTime;

public class CuidadoHeladerasRepository extends HibernateEntityManager<CuidadoHeladera, Long> {
  public Long getMesesActivosCumulativos(Colaborador colaborador) {
    String jpql = """
        SELECT COALESCE(SUM(CASE\s
            WHEN ch.heladera.fechaInstalacion <= :currentDate\s
            THEN FLOOR(DATEDIFF(:currentDate, ch.heladera.fechaInstalacion) / 30.44)\s
            ELSE 0 END), 0)\s
        FROM CuidadoHeladera ch\s
        WHERE ch.colaborador = :colaborador""";

    Double res = entityManager()
        .createQuery(jpql, Double.class)
        .setParameter("colaborador", colaborador)
        .setParameter("currentDate", ZonedDateTime.now())
        .getSingleResult();

    return res != null ? (long) res.doubleValue() : 0;
  }

  public int getTotal(Colaborador colaborador) {
    String jpql = """
        SELECT COUNT(ch)
        FROM CuidadoHeladera ch
        WHERE ch.colaborador = :colaborador
    """;

    Long result = entityManager()
            .createQuery(jpql, Long.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult();

    return result != null ? result.intValue() : 0;
  }

}
