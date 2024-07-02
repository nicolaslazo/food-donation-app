package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DonacionViandasController {
  private final DonacionViandasRepository donacionViandasRepository;
  public DonacionViandasController() {
    this.donacionViandasRepository = DonacionViandasRepository.getInstancia();
  }

  public List<DonacionViandas> obtenerDonacionesSemanaAnterior()
  {return donacionViandasRepository.obtenerDonacionesSemanaAnterior();}

  public static Map<String, Integer> mapDonacionesPorColaboradorSemanaAnterior(List<DonacionViandas> donaciones) {
    return donaciones.stream()
        .collect(Collectors.groupingBy(
            donacion -> donacion.getColaborador().getNombre() + " " + donacion.getColaborador().getApellido(),
            Collectors.summingInt(donacion -> donacion.getViandas().size())
        ));
  }
  public Map<String, Integer> obtenerDonacionesPorColaboradorSemanaAnterior()
  {List<DonacionViandas> donaciones = obtenerDonacionesSemanaAnterior();
  return mapDonacionesPorColaboradorSemanaAnterior(donaciones);}

}

