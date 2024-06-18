package ar.edu.utn.frba.dds.models.entities.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.EstadoFuncionamiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class PonerInactivaHeladera implements ComandoHeladera{

    @Override
    public void accionar(Heladera heladera) {
        heladera.setEstadoFuncionamiento(EstadoFuncionamiento.INACTIVA);
    }
}
