package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

public class Usuario {
  final @NonNull Email mail;
  @Getter
  final @NonNull Set<Rol> roles;
  @NonNull String contrasenia;

  public Usuario(Email mail, Set<Rol> roles) {
    this.mail = mail;
    this.contrasenia = GeneradorDeContrasenias.generarContrasenia();
    this.roles = roles;

    // TODO: Terminar el setup de envío de mails y configurar que enviarMail sea un no-op durante tests
    // new EnviadorDeMails().enviarMail(mail.destinatario(), this.contrasenia);
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return roles.stream().anyMatch(rol -> rol.tienePermiso(permiso));
  }

  public boolean tienePermiso(@NonNull String nombre) {
    // Asume que la descripción es null
    return tienePermiso(new Permiso(nombre, null));
  }

  public void assertTienePermiso(@NonNull String nombrePermiso, @NonNull String razon) throws PermisoDenegadoException {
    if (!tienePermiso(nombrePermiso)) throw new PermisoDenegadoException(razon);
  }
}
