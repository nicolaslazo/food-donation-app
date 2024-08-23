package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Colaborador {
  static final Rol ROL_DEFAULT = new Rol("colaborador", new HashSet<>(List.of(new Permiso("depositarViandas"))));
  @NonNull List<Contacto> contactos;
  @NonNull Usuario usuario;
  CoordenadasGeograficas ubicacion;

  public Colaborador(@NonNull Email mail,
                     @NonNull Documento documento,
                     @NonNull String primerNombre,
                     @NonNull String apellido,
                     LocalDate fechaNacimiento,
                     CoordenadasGeograficas ubicacion) {
    this.contactos = new ArrayList<>(List.of(mail));
    this.usuario = new Usuario(mail,
        documento,
        primerNombre,
        apellido,
        fechaNacimiento,
        new HashSet<>(List.of(ROL_DEFAULT)));
    this.ubicacion = ubicacion;
  }

  public void enviarMensaje(String mensaje) {
    contactos.forEach(contacto -> {
      try {
        contacto.enviarMensaje(mensaje);
      } catch (MensajeAContactoException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public UUID getId() {
    return usuario.getId();
  }

  public String getNombreCompleto() {
    return usuario.getPrimerNombre() + " " + usuario.getApellido();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Colaborador that = (Colaborador) o;
    return Objects.equals(getUsuario(), that.getUsuario());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsuario());
  }
}