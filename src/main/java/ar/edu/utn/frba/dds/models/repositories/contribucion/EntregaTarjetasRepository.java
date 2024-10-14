package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntregaTarjetasRepository extends HibernateEntityManager<EntregaTarjetas, Long> {
  public int getTotal(Colaborador colaborador) {
    String jpql = "SELECT COUNT(tr) FROM EntregaTarjetas et JOIN et.tarjetasSolicitadas tr WHERE et.colaborador = :colaborador";
    Long totalTarjetas = entityManager()
            .createQuery(jpql, Long.class)
            .setParameter("colaborador", colaborador)
            .getSingleResult();

    return totalTarjetas.intValue();
  }
}
