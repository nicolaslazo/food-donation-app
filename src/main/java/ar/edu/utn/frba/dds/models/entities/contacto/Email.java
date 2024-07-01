package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.services.mensajeria.mail.EnviadorMail;
import lombok.NonNull;

public record Email(@NonNull String destinatario) implements Contacto {
  public void enviarMensaje(String mensaje) {
    EnviadorMail.getInstancia().enviarMail(destinatario, mensaje);
  }
}