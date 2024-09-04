package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntregaTarjetasRepository {
  static EntregaTarjetasRepository instancia = null;
  final List<EntregaTarjetas> entregas;

  public EntregaTarjetasRepository() {
    this.entregas = new ArrayList<>();
  }

  public static EntregaTarjetasRepository getInstancia() {
    if (instancia == null) {
      instancia = new EntregaTarjetasRepository();
    }

    return instancia;
  }

  public Optional<EntregaTarjetas> get(int id) {
    return entregas.stream().filter(entrega -> entrega.getId() == id).findFirst();
  }

  public int getTotal(Colaborador colaborador) {
    return entregas
        .stream()
        .filter(entrega -> entrega.getColaborador() == colaborador)
        .mapToInt(EntregaTarjetas::getNumeroTarjetas)
        .sum();
  }

  public Long insert(EntregaTarjetas entrega) throws RepositoryException {
    if (entregas
        .stream()
        .flatMap(entregaAnterior -> entregaAnterior.getTarjetasRepartidas().stream())
        .anyMatch(entrega.getTarjetasRepartidas()::contains)) {
      throw new RepositoryException("Al menos una de estas tarjetas ya fue entregada en el pasado");
    }

    entregas.add(entrega);

    return entrega.getId();
  }

  public void deleteTodo() {
    entregas.clear();
  }
}
