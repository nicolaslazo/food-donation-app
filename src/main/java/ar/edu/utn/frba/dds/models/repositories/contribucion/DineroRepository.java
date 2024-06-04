package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DineroRepository {
  private static DineroRepository instancia = null;
  private final List<Dinero> donaciones;

  public DineroRepository() {
    this.donaciones = new ArrayList<>();
  }

  public static DineroRepository getInstancia() {
    if (instancia == null) {
      instancia = new DineroRepository();
    }

    return instancia;
  }

  public Optional<Dinero> get(int id) {
    return donaciones.stream().filter(donacion -> donacion.getId() == id).findFirst();
  }

  public double getTotal(Colaborador colaborador) {
    return donaciones
        .stream()
        .filter(donacion -> donacion.getColaborador() == colaborador)
        .mapToDouble(Dinero::getMonto)
        .sum();  // TODO: Implementar para donaciones recurrentes también
  }

  public int insert(Dinero dinero) {
    donaciones.add(dinero);
    dinero.setId(donaciones.size());

    return dinero.getId();
  }

  public void deleteTodo() {
    this.donaciones.clear();
  }
}
