package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
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

@Getter
public class Colaborador {
  private final static Rol ROL_DEFAULT = new Rol("colaborador", new HashSet<>(List.of(new Permiso("depositarViandas"))));
  @NonNull Documento documento;
  @NonNull List<Contacto> contactos;
  LocalDate fechaNacimiento;
  @NonNull Usuario usuario;
  @NonNull String nombre;
  @NonNull String apellido;
  Ubicacion ubicacion;

  public Colaborador(@NonNull Documento documento,
                     @NonNull Email mail,
                     LocalDate fechaNacimiento,
                     @NonNull String nombre,
                     @NonNull String apellido,
                     Ubicacion ubicacion) {
    this.documento = documento;
    this.contactos = new ArrayList<>(List.of(mail));
    this.usuario = new Usuario(mail, new HashSet<>(List.of(ROL_DEFAULT)));
    this.fechaNacimiento = fechaNacimiento;
    this.nombre = nombre;
    this.apellido = apellido;
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

  public String getNombreCompleto() {
    return nombre + " " + apellido;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Colaborador that = (Colaborador) o;
    return Objects.equals(getDocumento(), that.getDocumento());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDocumento());
  }

  @Override
  public String toString() {
    return "Colaborador{" +
        "documento=" +
        documento + ", nombre='" +
        nombre + '\'' + ", apellido='" +
        apellido + '\'' +
        '}';
  }
}