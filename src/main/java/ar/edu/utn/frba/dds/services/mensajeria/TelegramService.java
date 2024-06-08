package ar.edu.utn.frba.dds.services.mensajeria;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import ar.edu.utn.frba.dds.models.repositories.contacto.TelegramRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramService extends TelegramLongPollingBot {
  private static TelegramService instancia = null;

  public static TelegramService getInstancia() throws TelegramApiException {
    if (instancia == null) {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      instancia = new TelegramService();
      botsApi.registerBot(instancia);
    }

    return instancia;
  }

  public static void main(String[] args) throws TelegramApiException, RepositoryInsertException {
    TelegramRepository.getInstancia().insert(new Telegram("sawtooth_waves"));
    TelegramService servicio = TelegramService.getInstancia();

    servicio.enviarMensaje(533241073L, "Corriendo bot");
  }

  @Override
  public String getBotUsername() {
    return "Servicio de Alertas para Heladeras Comunitarias";
  }

  @Override
  public String getBotToken() {
    return ConfigLoader.getInstancia().getProperty("telegram.api.key");
  }

  /*
   * Long quien representa el chat ID del recipiente
   */
  public void enviarMensaje(Long quien, String que) {
    SendMessage sm = SendMessage.builder().chatId(quien.toString()).text(que).build();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onUpdateReceived(Update update) {
    Message mensaje = update.getMessage();
    String username = mensaje.getFrom().getUserName();

    System.out.println("Mensaje recibido. Autor: " + username + ". Texto: " + mensaje.getText());

    if (!mensaje.getText().equals("/registrar")) return;

    long id = mensaje.getChatId();

    try {
      TelegramRepository.getInstancia().updateChatId(username, id);
    } catch (RepositoryInsertException e) {
      enviarMensaje(id, "Hubo un problema en el proceso de registro. Por favor intente más tarde");
    }

    System.out.println("Registro completado. ChatId: " + id);
    enviarMensaje(id, "Registro completado con éxito. Muchas gracias por usar nuestro servicio!");
  }
}
