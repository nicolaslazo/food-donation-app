package ar.edu.utn.frba.dds.models.entities.contacto;

import lombok.NonNull;

public record ContactoEmail(@NonNull String destinatario) implements Contacto {
  public void enviarMensaje(String mensaje) {
    // TODO implement here
  }
}