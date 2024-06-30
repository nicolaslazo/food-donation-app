package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.sensores.comandos.ComandoHeladera;

import java.util.ArrayList;
import java.util.List;

public class AccionadorHeladera {
    private List<ComandoHeladera> comandosHeladeras;

    public AccionadorHeladera() {
        comandosHeladeras = new ArrayList<>();
    }

    public AccionadorHeladera(List<ComandoHeladera> comandosHeladeras) {
        this.comandosHeladeras = comandosHeladeras;
    }

    public CargarAlertaEnIncidentes cargadorAlertas;

    private void accionar(Heladera heladera) {
        this.comandosHeladeras.forEach(c -> c.accionar(heladera));
    }


    //TODO se me ocurre una sobre carga de este metodo para la opcion de que no sea una Alerta
    // y quieras registrar una falla tecnica por un colaborador
    private void registrarIncidente(TipoIncidente tipoIncidente, Heladera heladera) {
        cargadorAlertas.getIntancia().cargarIncidente(
                tipoIncidente,heladera
        );
    }

    //Es el metodo que se llama a la hora de detectar algun tipo de Incidente
    public void sucedeIncidente(TipoIncidente tipoIncidente, Heladera heladera) {
        this.registrarIncidente(tipoIncidente,heladera);
        this.accionar(heladera);
    }

    public void agregarComando(ComandoHeladera comandoHeladera) {
        comandosHeladeras.add(comandoHeladera);
    }
}
