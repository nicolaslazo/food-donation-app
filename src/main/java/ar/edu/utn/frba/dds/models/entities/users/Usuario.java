package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import lombok.Getter;
import lombok.NonNull;

public class Usuario {
  final @NonNull Email mail;
  @NonNull String contrasenia;
  @Getter
  @NonNull Rol rol;

  public Usuario(Email mail, Rol rol) {
    this.mail = mail;
    this.contrasenia = GeneradorDeContrasenias.generarContrasenia();
    this.rol = rol;

    // TODO: Terminar el setup de envío de mails y configurar que enviarMail sea un no-op durante tests
    // new EnviadorDeMails().enviarMail(mail.destinatario(), this.contrasenia);
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return rol.tienePermiso(permiso);
  }

  public boolean tienePermiso(@NonNull String nombre) {
    // Asume que la descripción es null
    return tienePermiso(new Permiso(nombre, null));
  }

  public void assertTienePermiso(@NonNull Permiso permiso, @NonNull String razon) {
    if (!tienePermiso(permiso)) throw new PermisoDenegadoException(razon);
  }

  public void assertTienePermiso(@NonNull String nombrePermiso, @NonNull String razon) {
    if (!tienePermiso(nombrePermiso)) throw new PermisoDenegadoException(razon);
  }
}
