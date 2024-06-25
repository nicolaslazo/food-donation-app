package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DonacionViandasRepository {
  private static DonacionViandasRepository instancia = null;
  private static List<DonacionViandas> donaciones;

  public DonacionViandasRepository() {
    donaciones = new ArrayList<>();
  }

  public static DonacionViandasRepository getInstancia() {
    if (instancia == null) {
      instancia = new DonacionViandasRepository();
    }

    return instancia;
  }

  public Optional<DonacionViandas> get(int id) {
    return donaciones.stream().filter(donacion -> donacion.getId() == id).findFirst();
  }

  public int getTotal(Colaborador colaborador) {
    return donaciones
        .stream()
        .filter(donacion -> donacion.getColaborador() == colaborador)
        .mapToInt(DonacionViandas::getNumeroViandas)
        .sum();
  }

  public int insert(DonacionViandas donacion) throws RepositoryInsertException {
    if (donaciones
        .stream()
        .flatMap(donacionPrevia -> donacionPrevia.getViandas().stream())
        .anyMatch(donacion.getViandas()::contains)) {
      throw new RepositoryInsertException("Al menos una de las viandas a insertar ya fue registrada en una donación previa");
    }

    donaciones.add(donacion);
    donacion.setId(donaciones.size());

    return donacion.getId();
  }

  public static Map<String, Integer> calcularViandasPorColaboradorSemanaAnterior() {
    ZonedDateTime hoy = ZonedDateTime.now();
    ZonedDateTime fechaSemanaAnterior = hoy.minusWeeks(1);

    Map<String, Integer> viandasPorColaborador = donaciones.stream()
        .filter(donacion -> donacion.getFecha().isAfter(fechaSemanaAnterior))
        .filter(donacion -> donacion.getFecha().isBefore(hoy))
        .collect(Collectors.groupingBy(
            donacion -> donacion.getColaborador().getNombre() + " " + donacion.getColaborador().getApellido(),
            Collectors.summingInt(DonacionViandas::getNumeroViandas)
        ));

    return viandasPorColaborador;
  }

  public void deleteTodo() {
    donaciones.clear();
  }
}
