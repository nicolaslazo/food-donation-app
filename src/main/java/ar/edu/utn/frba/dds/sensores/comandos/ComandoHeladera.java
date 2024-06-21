package ar.edu.utn.frba.dds.sensores.comandos;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public interface ComandoHeladera {

    //TODO ¿deberia de recibir la heladera como argumento?
    public void accionar(Heladera heladera);
}
