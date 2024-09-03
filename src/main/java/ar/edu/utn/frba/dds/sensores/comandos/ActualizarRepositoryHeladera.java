package ar.edu.utn.frba.dds.sensores.comandos;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

public class ActualizarRepositoryHeladera implements ComandoHeladera {
  @Override
  public void accionar(Heladera heladera) {
    new HeladerasRepository().updateTiempoHeladera(heladera.getId(), heladera.getUltimaTempRegistradaCelsius() );
  }
}
