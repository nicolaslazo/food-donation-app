package ar.edu.utn.frba.dds.domain.contacto;

import lombok.NonNull;

public class ContactoEmail implements Contacto {
  @NonNull
  private String destinatario;

  public ContactoEmail(@NonNull String destinatario) {
    this.destinatario = destinatario;
  }

  public void enviarMensaje(String mensaje) {
    // TODO implement here
  }
}