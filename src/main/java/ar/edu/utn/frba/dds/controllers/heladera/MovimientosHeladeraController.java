package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.MovimientosHeladeraRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientosHeladeraController {

  private final MovimientosHeladeraRepository movimientosHeladeraRepository;

  public MovimientosHeladeraController() {
    this.movimientosHeladeraRepository = MovimientosHeladeraRepository.getInstancia();
  }

  public  List<EventoMovimiento> obtenerMovimientosSemanaAnterior()
  {
    return movimientosHeladeraRepository.obtenerMovimientosSemanaAnterior();
  }

  public static Map<String, Integer> mapCantidadMovimientosPorHeladera(List<EventoMovimiento> eventos) {
    Map<String, Integer> cantidadMovimientos = new HashMap<>();
    for (EventoMovimiento evento : eventos) {
      Heladera heladera = evento.getHeladera();
      cantidadMovimientos.put(heladera.getNombre(), cantidadMovimientos.getOrDefault(heladera, 0) + 1);
    }
    return cantidadMovimientos;
  }

  public Map<String, Integer>obtenerCantidadMovimientosHeladeraSemanaAnterior()
  {
    return mapCantidadMovimientosPorHeladera(obtenerMovimientosSemanaAnterior());
  }


}
