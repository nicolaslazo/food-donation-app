package ar.edu.utn.frba.dds.models.entities.sensores;

import java.util.List;

public class AccionadorHeladera {
    private List<ComandoHeladera> comandosHeladeras;

    public AccionadorHeladera(List<ComandoHeladera> comandosHeladeras) {
        this.comandosHeladeras = comandosHeladeras;
    }


    private void accionar() {
        //TODO ¿deberia de mandar la heladera?
        this.comandosHeladeras.forEach(c -> c.accionar());
    }

    private void registrarIncidente(TipoAlertaHeladera tipoAlertaHeladera) {
        //TODO deberiamos de utilizar el repositorio de los Incidentes y guardarlo ahi
    }

    //Es el metodo que se llama a la hora de detectar algun tipo de Incidente
    public void sucedeIncidente(TipoAlertaHeladera tipoAlertaHeladera) {
        this.registrarIncidente(tipoAlertaHeladera);
        this.accionar();
    }

    public void agregarComando(ComandoHeladera comandoHeladera) {
        comandosHeladeras.add(comandoHeladera);
    }
}
