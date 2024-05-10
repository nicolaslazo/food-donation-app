package ar.edu.utn.frba.dds.domain.colaborador;

import ar.edu.utn.frba.dds.domain.Heladera;
import ar.edu.utn.frba.dds.domain.contacto.Contacto;
import ar.edu.utn.frba.dds.domain.documentacion.Documento;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Colaborador {
  @Getter
  private Documento documento;
  private String apellido;
  private String nombre;
  private LocalDate fechaNacimiento;
  private Ubicacion ubicacion;
  private List<Contacto> contactos = new ArrayList<>();
  private List<Heladera> heladerasACargo = new ArrayList<>();
  private int tarjetasAlimentariasEntregadas = 0;

  public Colaborador(Documento documento, String nombre, String apellido) {
  }

  public TipoColaborador getTipoColaborador() {
    if (documento != null) return TipoColaborador.HUMANO;
    return TipoColaborador.JURIDICO;
  }

  public void colaborar(Contribucion contribucion) {
    // TODO implement here
  }

  public float calcularReconocimiento() {
    // TODO implement here
    return 0.0f;
  }
}