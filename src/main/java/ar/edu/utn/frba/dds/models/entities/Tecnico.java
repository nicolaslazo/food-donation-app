package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;


public final class Tecnico {
  private final @NonNull Documento documento;
  @Getter
  private final @NonNull String cuil;
  private final @NonNull List<Contacto> contactos;
  private final @NonNull Usuario usuario;
  private @NonNull String nombre;
  private @NonNull String apellido;
  @Getter
  private @NonNull AreaGeografica areaAsignada;

  public Tecnico(@NonNull String nombre, @NonNull String apellido, @NonNull Documento documento,
                 @NonNull String cuil, List<Contacto> contactos, @NonNull AreaGeografica areaAsignada,
                 @NonNull Usuario usuario) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.documento = documento;
    this.cuil = cuil;
    this.contactos = contactos != null ? contactos : new ArrayList<>();
    this.areaAsignada = areaAsignada;
    this.usuario = usuario;
  }

  public boolean isDentroDeRango(Heladera heladera) {
    return CalculadoraDistancia.calcular(heladera.getUbicacion(), areaAsignada.centro()) < areaAsignada.radioEnMetros();
  }
}