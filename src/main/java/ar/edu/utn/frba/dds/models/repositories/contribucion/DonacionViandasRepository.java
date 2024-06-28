package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonacionViandasRepository {
  private static DonacionViandasRepository instancia = null;
  private final List<DonacionViandas> donaciones;

  public DonacionViandasRepository() {
    this.donaciones = new ArrayList<>();
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

  public int insert(DonacionViandas donacion) throws RepositoryException {
    if (donaciones
        .stream()
        .flatMap(donacionPrevia -> donacionPrevia.getViandas().stream())
        .anyMatch(donacion.getViandas()::contains)) {
      throw new RepositoryException("Al menos una de las viandas a insertar ya fue registrada en una donación previa");
    }

    donaciones.add(donacion);
    donacion.setId(donaciones.size());

    return donacion.getId();
  }

  public void deleteTodo() {
    donaciones.clear();
  }
}
