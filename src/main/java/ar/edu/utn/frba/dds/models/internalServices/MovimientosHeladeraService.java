package ar.edu.utn.frba.dds.models.internalServices;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.MovimientosHeladeraRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovimientosHeladeraService {

  private final MovimientosHeladeraRepository movimientosHeladeraRepository;

  public MovimientosHeladeraService() {
    this.movimientosHeladeraRepository = MovimientosHeladeraRepository.getInstancia();
  }

  public static MovimientosHeladeraService getInstance() {
    return MovimientosHeladeraService.SingletonHelper.INSTANCE;
  }

  private static class SingletonHelper {
    private static final MovimientosHeladeraService INSTANCE = new MovimientosHeladeraService();
  }

  public Map<String, Integer> obtenerCantidadMovimientosPorHeladeraSemanaAnterior() {
    List <EventoMovimiento> eventos = movimientosHeladeraRepository.obtenerMovimientosSemanaAnterior();

    Map<String, Integer> cantidadMovimientos = new HashMap<>();

    for (EventoMovimiento evento : eventos) {
      Heladera heladera = evento.getHeladera();
      cantidadMovimientos.put(heladera.getNombre(), cantidadMovimientos.getOrDefault(heladera, 0) + 1);
    }

    return cantidadMovimientos;
  }

}
