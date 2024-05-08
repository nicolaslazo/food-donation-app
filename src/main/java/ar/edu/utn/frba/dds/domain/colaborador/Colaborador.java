package ar.edu.utn.frba.dds.domain.colaborador;

import ar.edu.utn.frba.dds.domain.Heladera;
import ar.edu.utn.frba.dds.domain.contacto.Contacto;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class Colaborador {
  private String apellido;
  private String nombre;
  private ZonedDateTime fechaNacimiento;
  private Ubicacion ubicacion;
  private List<Contacto> contactos;
  private TipoColaborador tipoColaborador;
  private List<Heladera> heladerasACargo;
  private int tarjetasAlimentariasEntregadas;

  public Colaborador() {
  }

  public void colaborar(Contribucion contribucion) {
    // TODO implement here
  }

  public float calcularReconocimiento() {
    // TODO implement here
    return 0.0f;
  }
}