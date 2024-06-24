package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HeladeraController {
  public Optional<Tecnico> encontrarTecnicoMasCercano(Heladera heladera) {
    final Ubicacion coordsHeladera = heladera.getUbicacion();

    HashMap<Tecnico, Double> distanciasAHeladera = new HashMap<>();
    TecnicoRepository
        .getInstancia()
        .getTecnicos()
        .stream()
        .filter(tecnico -> tecnico.isDentroDeRango(heladera))
        .forEach(tecnico ->
            distanciasAHeladera.put(
                tecnico,
                CalculadoraDistancia.calcular(tecnico.getAreaAsignada().centro(), coordsHeladera)
            )
        );

    return distanciasAHeladera
        .entrySet()
        .stream()
        .reduce((una, otra) -> una.getValue() < otra.getValue() ? una : otra)
        .map(Map.Entry::getKey);
  }
}
