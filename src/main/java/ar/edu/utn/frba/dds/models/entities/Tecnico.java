package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import lombok.Getter;

import java.util.List;


public class Tecnico {
  private String nombre;
  private String apellido;
  private Documento documento;
  @Getter
  private final String cuil;
  private List<Contacto> contactos;
  @Getter
  private AreaGeografica areaAsignada;

  public Tecnico(String cuil, AreaGeografica areaAsignada) {
    this.cuil = cuil;
    this.areaAsignada = areaAsignada;
  }

  public boolean isDentroDeRango(Heladera heladera) {
    return CalculadoraDistancia.calcular(heladera.getUbicacion(), areaAsignada.centro()) < areaAsignada.radioEnMetros();
  }
}
