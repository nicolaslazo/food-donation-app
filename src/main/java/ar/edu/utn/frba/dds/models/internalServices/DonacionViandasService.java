package ar.edu.utn.frba.dds.models.entities.internalServices;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DonacionViandasService {

  private final DonacionViandasRepository donacionViandasRepository;

  public DonacionViandasService(DonacionViandasRepository donationRepository) {
    this.donacionViandasRepository = donationRepository;
  }

  public Map<String, Integer> obtenerDonacionesPorColaboradorSemanaAnterior() {
    ZonedDateTime fechaActual = ZonedDateTime.now();
    ZonedDateTime inicioSemanaAnterior = fechaActual.minusWeeks(1);


    List<DonacionViandas> donaciones = donacionViandasRepository.obtenerDonacionesSemanaAnterior();
    return donaciones.stream()
        .filter(donacion -> donacion.getFecha().isAfter(inicioSemanaAnterior) && donacion.getFecha().isBefore(fechaActual))
        .collect(Collectors.groupingBy(
            donacion -> donacion.getColaborador().getNombre() + " " + donacion.getColaborador().getApellido(),
            Collectors.summingInt(donacion -> donacion.getViandas().size())
        ));
  }


}
