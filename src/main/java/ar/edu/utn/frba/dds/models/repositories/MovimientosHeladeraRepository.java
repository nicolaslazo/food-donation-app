package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;

import java.util.ArrayList;
import java.util.List;

public class MovimientosHeladeraRepository {
  private final List<EventoMovimiento> movimientos;

  public MovimientosHeladeraRepository() {
    this.movimientos = new ArrayList<>();
  }

  public int insert(EventoMovimiento evento) {
    evento.setId(movimientos.size() + 1);
    movimientos.add(evento);

    return evento.getId();
  }
}
