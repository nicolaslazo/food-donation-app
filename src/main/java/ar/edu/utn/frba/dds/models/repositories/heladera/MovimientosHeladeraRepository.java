package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovimientosHeladeraRepository {

  private static MovimientosHeladeraRepository instancia = null;
  private final List<EventoMovimiento> movimientos;

  public MovimientosHeladeraRepository() {
    this.movimientos = new ArrayList<>();
  }

  public static MovimientosHeladeraRepository getInstancia() {
    if (instancia == null) {
      instancia = new MovimientosHeladeraRepository();
    }
    return instancia;
  }

  public List<EventoMovimiento> obtenerMovimientosSemanaAnterior() {
    ZonedDateTime fechaActual = ZonedDateTime.now();
    ZonedDateTime inicioSemanaAnterior = fechaActual.minusWeeks(1);

    return movimientos.stream()
        .filter(movimiento -> movimiento.getFecha().isAfter(inicioSemanaAnterior))
        .collect(Collectors.toList());
  }

  public int insert(EventoMovimiento evento) {
    evento.setId(movimientos.size() + 1);
    movimientos.add(evento);

    return evento.getId();
  }
}
