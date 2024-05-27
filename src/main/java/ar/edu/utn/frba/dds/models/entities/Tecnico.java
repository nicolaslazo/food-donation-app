package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class Tecnico {
  private String nombre;
  private String apellido;
  private Documento documento;
  @Getter
  private String cuil;
  private List<Contacto> contactos;
  private AreaGeografica areaAsignada;

  public Tecnico(String cuil) {
    this.cuil = cuil;
  }
}
