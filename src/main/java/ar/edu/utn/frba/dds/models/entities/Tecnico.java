package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public final class Tecnico {
  // TODO: el rol capaz debería estar almacenado en un repositorio?
  private static final Rol ROL_DEFAULT = new Rol("tecnico", new HashSet<>());
  @Getter
  final @NonNull String cuil;
  final @NonNull List<Contacto> contactos;
  final @NonNull Usuario usuario;
  @Getter
  @NonNull AreaGeografica areaAsignada;

  public Tecnico(@NonNull Email email,
                 @NonNull Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 @NonNull LocalDate fechaNacimiento,
                 @NonNull String cuil,
                 @NonNull AreaGeografica areaAsignada) {
    this.usuario = new Usuario(email,
        documento,
        primerNombre,
        apellido,
        fechaNacimiento,
        new HashSet<>(List.of(ROL_DEFAULT)));  // TODO: Agregar permisos
    this.cuil = cuil;
    this.areaAsignada = areaAsignada;
    this.contactos = new ArrayList<>(List.of(email));
  }

  public void enviarMensaje(String mensaje) throws MensajeAContactoException {
    for (Contacto contacto : contactos) {
      contacto.enviarMensaje(mensaje);
    }
  }

  public boolean isDentroDeRango(Heladera heladera) {
    return CalculadoraDistancia.calcular(heladera.getUbicacion(), areaAsignada.centro()) < areaAsignada.radioEnMetros();
  }
}