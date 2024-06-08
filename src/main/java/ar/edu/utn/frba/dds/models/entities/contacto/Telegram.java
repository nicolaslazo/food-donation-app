package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.services.mensajeria.TelegramService;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
public class Telegram implements Contacto {
  private final String usuario;
  @Setter
  private Long chatId = null;

  public Telegram(String usuario) {
    this.usuario = usuario;
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
        "usuario='" + usuario + '\'' +
        ", chatId=" + chatId +
        '}';
  }
}
