package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.services.mensajeria.mail.EnviadorMail;
import lombok.NonNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EMAIL")
public class Email extends Contacto {
  public Email(@NonNull Usuario usuario, @NonNull String direccion) {
    this.usuario = usuario;
    this.destinatario = direccion;
  }

  protected Email() {
  }

  public void enviarMensaje(String mensaje) {
    EnviadorMail.getInstancia().enviarMail(destinatario, mensaje);
  }
}