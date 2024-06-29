package ar.edu.utn.frba.dds.models.internalServices;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DonacionViandasService {
  private final DonacionViandasRepository donacionViandasRepository;
  public DonacionViandasService() {
    this.donacionViandasRepository = DonacionViandasRepository.getInstancia();
  }

  public static DonacionViandasService getInstance() {
    return SingletonHelper.INSTANCE;
  }

  private static class SingletonHelper {
    private static final DonacionViandasService INSTANCE = new DonacionViandasService();
  }
  public Map<String, Integer> obtenerDonacionesPorColaboradorSemanaAnterior() {
    List<DonacionViandas> donaciones = donacionViandasRepository.obtenerDonacionesSemanaAnterior();
    return donaciones.stream()
        .collect(Collectors.groupingBy(
            donacion -> donacion.getColaborador().getNombre() + " " + donacion.getColaborador().getApellido(),
            Collectors.summingInt(donacion -> donacion.getViandas().size())
        ));
  }
}
