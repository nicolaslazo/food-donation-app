package ar.edu.utn.frba.dds.models.entities.sensores;

public class AccionadorHeladera {

    private void accionar() {
        //TODO
    }

    private void registrarIncidente(TipoIncidenteHeladera tipoIncidenteHeladera) {
        //TODO deberiamos de utilizar el repositorio de los Incidentes y guardarlo ahi
    }

    //Es el metodo que se llama a la hora de detectar algun tipo de Incidente
    public void sucedeIncidente(TipoIncidenteHeladera tipoIncidenteHeladera) {
        this.registrarIncidente(tipoIncidenteHeladera);
        this.accionar();
    }
}
