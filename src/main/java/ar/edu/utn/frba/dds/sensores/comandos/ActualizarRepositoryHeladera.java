package ar.edu.utn.frba.dds.sensores.comandos;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

public class ActualizarRepositoryHeladera implements ComandoHeladera {
    private HeladerasRepository heladerasRepository;

    @Override
    public void accionar(Heladera heladera) {
        heladerasRepository.updateHeladera(heladera);
    }
}
