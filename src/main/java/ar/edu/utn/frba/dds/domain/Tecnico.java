package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.contacto.Contacto;
import ar.edu.utn.frba.dds.domain.documentacion.Documento;
import ar.edu.utn.frba.dds.domain.ubicacion.AreaDeCobertura;

import java.util.List;

public class Tecnico {
  private String nombre;
  private String apellido;
  private Documento documento;
  private String cuil;
  private List<Contacto> contactos;
  private AreaDeCobertura areaAsignada;

  public Tecnico() {
  }
}
