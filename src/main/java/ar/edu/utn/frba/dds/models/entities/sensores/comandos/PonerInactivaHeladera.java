package ar.edu.utn.frba.dds.models.entities.sensores.comandos;

import ar.edu.utn.frba.dds.models.entities.heladera.EstadoFuncionamiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class PonerInactivaHeladera implements ComandoHeladera {
    public PonerInactivaHeladera() {}

    @Override
    public void accionar(Heladera heladera) {
        heladera.setEstadoFuncionamiento(EstadoFuncionamiento.INACTIVA);
    }
}
