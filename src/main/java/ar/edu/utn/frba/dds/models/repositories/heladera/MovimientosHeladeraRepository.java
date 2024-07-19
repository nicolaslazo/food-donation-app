package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  public Map<Heladera, Integer> getCantidadMovimientosPorHeladeraSemanaAnterior() {
    Map<Heladera, Integer> cantidadMovimientos = new HashMap<>();

    for (EventoMovimiento evento : getMovimientosSemanaAnterior()) {
      Heladera heladera = evento.getHeladera();
      cantidadMovimientos.put(heladera, cantidadMovimientos.getOrDefault(heladera, 0) + 1);
    }

    return cantidadMovimientos;
  }

  public List<EventoMovimiento> getMovimientosSemanaAnterior() {
    ZonedDateTime haceUnaSemana = ZonedDateTime.now().minusWeeks(1);

    return movimientos.stream()
        .filter(movimiento -> movimiento.getFecha().isAfter(haceUnaSemana))
        .collect(Collectors.toList());
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
