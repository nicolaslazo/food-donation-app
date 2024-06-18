package ar.edu.utn.frba.dds.models.entities.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import java.util.List;

public class AccionadorHeladera {
    private List<ComandoHeladera> comandosHeladeras;

    public AccionadorHeladera(List<ComandoHeladera> comandosHeladeras) {
        this.comandosHeladeras = comandosHeladeras;
    }


    private void accionar(Heladera heladera) {
        //TODO ¿deberia de mandar la heladera?
        this.comandosHeladeras.forEach(c -> c.accionar(heladera));
    }


    //TODO se me ocurre una sobre carga de este metodo para la opcion de que no sea una Alerta
    // y quieras registrar una falla tecnica por un colaborador
    private void registrarIncidente(TipoAlertaHeladera tipoAlertaHeladera) {
        //TODO deberiamos de utilizar el repositorio de los Incidentes y guardarlo ahi
        //TODO podriamos utilizar un Builder para crear el TipoIncidente
    }

    //Es el metodo que se llama a la hora de detectar algun tipo de Incidente
    public void sucedeIncidente(TipoAlertaHeladera tipoAlertaHeladera, Heladera heladera) {
        this.registrarIncidente(tipoAlertaHeladera);
        this.accionar(heladera);
    }

    public void agregarComando(ComandoHeladera comandoHeladera) {
        comandosHeladeras.add(comandoHeladera);
    }
}
