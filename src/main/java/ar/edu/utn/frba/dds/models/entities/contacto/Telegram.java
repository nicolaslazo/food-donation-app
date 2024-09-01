package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.services.mensajeria.TelegramService;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TELEGRAM")
@Getter
@Setter
public class Telegram extends Contacto {
  @Column(name = "chatId", unique = true)
  Long chatId = null;

  public Telegram(@NonNull Usuario usuario, @NonNull String username) {
    this.usuario = usuario;
    this.destinatario = username;
  }

  protected Telegram() {
  }

  public void enviarMensaje(String mensaje) throws MensajeAContactoException {
    if (chatId == null) {
      throw new MensajeAContactoException("El contacto todavía no fue registrado en el bot de Telegram");
    }

    try {
      TelegramService.getInstancia().enviarMensaje(chatId, mensaje);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "Telegram{" +
        "chatId=" + chatId +
        ", colaborador=" + usuario +
        ", destinatario='" + destinatario + '\'' +
        '}';
  }
}
