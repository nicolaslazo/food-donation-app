package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Contribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DonacionViandasRepository extends HibernateEntityManager<DonacionViandas, Long> {
  // Obtener cantidad de donaciones por colaborador en la semana anterior
  public Map<Colaborador, Long> getCantidadDonacionesPorColaboradorSemanaAnterior() {
    ZonedDateTime haceUnaSemana = ZonedDateTime.now().minusWeeks(1);

    // Query HQL para obtener donaciones por colaborador desde la semana pasada
    TypedQuery<DonacionViandas> query = entityManager().createQuery(
            "SELECT d FROM DonacionViandas d WHERE d.fechaRealizada > :fecha", DonacionViandas.class);
    query.setParameter("fecha", haceUnaSemana);

    // Agrupar por colaborador y contar la cantidad de viandas donadas
    return query.getResultStream()
            .collect(Collectors.groupingBy(
                    DonacionViandas::getColaborador,
                    Collectors.summingLong(donacion -> donacion.getViandas().size())
            ));
  }

  // Obtener el total de viandas donadas por un colaborador específico
  public int getTotal(Colaborador colaborador) {
    // Query HQL para contar viandas de un colaborador específico
    TypedQuery<Long> query = entityManager().createQuery(
            "SELECT SUM(d.viandas.size) FROM DonacionViandas d WHERE d.colaborador = :colaborador", Long.class);
    query.setParameter("colaborador", colaborador);

    return query.getSingleResult() != null ? query.getSingleResult().intValue() : 0;
  }

//  public Long insert(DonacionViandas donacion) throws RepositoryException {
//    if (donaciones
//        .stream()
//        .flatMap(donacionPrevia -> donacionPrevia.getViandas().stream())
//        .anyMatch(donacion.getViandas()::contains)) {
//      throw new RepositoryException("Al menos una de las viandas a insertar ya fue registrada en una donación previa");
//    }
//    donaciones.add(donacion);
//    donacion.setId((long) donaciones.size());
//
//    return donacion.getId();
//  }

}
