package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;

import java.util.ArrayList;
import java.util.List;

public class MovimientosHeladeraRepository {

  private static MovimientosHeladeraRepository instancia = null;
  private final List<EventoMovimiento> movimientos;

  private MovimientosHeladeraRepository() {
    this.movimientos = new ArrayList<>();
  }

  public static MovimientosHeladeraRepository getInstancia() {
    if (instancia == null) instancia = new MovimientosHeladeraRepository();

    return instancia;
  }

  public int insert(EventoMovimiento evento) {
    evento.setId(movimientos.size() + 1);
    movimientos.add(evento);

    return evento.getId();
  }

  public void deleteTodos() {
    movimientos.clear();
  }
}
