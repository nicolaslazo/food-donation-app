package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.sensores.comandos.ComandoHeladera;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccionadorHeladera {
  public IncidenteController instanciaController = IncidenteController.getInstancia();
  private List<ComandoHeladera> comandosHeladeras;

  public AccionadorHeladera() {
    comandosHeladeras = new ArrayList<>();
  }

  private void accionar(Heladera heladera) {
    this.comandosHeladeras.forEach(c -> c.accionar(heladera));
  }


  private void registrarIncidente(TipoIncidente tipoIncidente, Heladera heladera, ZonedDateTime momentoEvento) {
    instanciaController.crearAlerta(heladera, tipoIncidente, momentoEvento);
  }

  //Es el metodo que se llama a la hora de detectar algun tipo de Incidente
  public void sucedeIncidente(TipoIncidente tipoIncidente, Heladera heladera, ZonedDateTime momentoEvento) {
    this.registrarIncidente(tipoIncidente, heladera, momentoEvento);
    this.accionar(heladera);
  }

  public void agregarComando(ComandoHeladera comandoHeladera) {
    comandosHeladeras.add(comandoHeladera);
  }
}
