package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import lombok.Getter;
import lombok.NonNull;

public class Usuario {
  private final @NonNull Email mail;
  private @NonNull String contrasenia;
  @Getter
  private @NonNull Rol rol;

  public Usuario(Email mail, Rol rol) {
    this.mail = mail;
    this.contrasenia = GeneradorDeContrasenias.generarContrasenia();
    this.rol = rol;

    // TODO: Terminar el setup de envío de mails y configurar que enviarMail sea un no-op durante tests
    // new EnviadorDeMails().enviarMail(mail.destinatario(), this.contrasenia);
  }
}
