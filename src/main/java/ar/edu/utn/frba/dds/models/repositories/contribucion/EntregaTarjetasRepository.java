package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EntregaTarjetasRepository extends HibernateEntityManager<EntregaTarjetas, Long> {
  public Stream<EntregaTarjetas> findAll(Colaborador colaborador) {
    // Tenemos que contabilizar las tarjetas entregadas, no las solicitidas.
    String jpql = """
            SELECT et
            FROM EntregaTarjetas et
            WHERE et.colaborador = :colaborador
            """;

    Stream<EntregaTarjetas> entregaTarjetas = entityManager()
            .createQuery(jpql, EntregaTarjetas.class)
            .setParameter("colaborador", colaborador)
            .getResultStream();

    return entregaTarjetas;
  }

  public int getTotal(Colaborador colaborador) {
    // Tenemos que contabilizar las tarjetas entregadas, no las solicitidas.
    String jpql = """
            SELECT COUNT(tr)
            FROM EntregaTarjetas et
            JOIN et.tarjetasSolicitadas tr
            WHERE et.colaborador = :colaborador
              AND tr.recipiente IS NOT NULL
            """;

    Long totalTarjetas = entityManager()
            .createQuery(jpql, Long.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult();

    return totalTarjetas != null ? totalTarjetas.intValue() : 0;
  }
}

