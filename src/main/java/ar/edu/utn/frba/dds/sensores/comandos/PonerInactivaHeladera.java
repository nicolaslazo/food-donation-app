package ar.edu.utn.frba.dds.sensores.comandos;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class PonerInactivaHeladera implements ComandoHeladera {
    public PonerInactivaHeladera() {}

    @Override
    public void accionar(Heladera heladera) {
        heladera.setHeladeraActiva(false);
    }
}
