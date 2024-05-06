package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;

import java.time.ZonedDateTime;

public class PersonaVulnerable extends Colaborador {

  public Colaborador reclutante;
  private String nombre;
  private String apellido;
  private ZonedDateTime fechaNacimiento;
  private ZonedDateTime fechaRegistrada;
  private Ubicacion ubicacion;
  private Documento documento;
  private int menoresACargo;

  private PersonaVulnerable() {
  }

  public void solicitarVianda(Vianda vianda, Heladera heladera) {
    // TODO implement here
  }
}