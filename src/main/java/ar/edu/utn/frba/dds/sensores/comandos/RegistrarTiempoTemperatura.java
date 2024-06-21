package ar.edu.utn.frba.dds.sensores.comandos;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class RegistrarTiempoTemperatura implements ComandoHeladera{
    public RegistrarTiempoTemperatura() {}

    @Override
    public void accionar(Heladera heladera) {
        //Este metodo registra el momento actual
        heladera.setUltimaTempRegistradaCelsius();
    }
}
