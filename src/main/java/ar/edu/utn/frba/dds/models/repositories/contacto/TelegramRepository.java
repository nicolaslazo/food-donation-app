package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelegramRepository {
  static TelegramRepository instancia = null;
  final List<Telegram> contactos;

  public TelegramRepository() {
    contactos = new ArrayList<>();
  }

  public static TelegramRepository getInstancia() {
    if (instancia == null) {
      instancia = new TelegramRepository();
    }

    return instancia;
  }

  public Optional<Telegram> get(String username) {
    return contactos.stream().filter(contacto -> contacto.getUsuario().equals(username)).findFirst();
  }

  public void insert(Telegram contacto) throws RepositoryInsertException {
    if (get(contacto.getUsuario()).isPresent()) {
      throw new RepositoryInsertException("Otro contacto con ese usuario ya existe");
    }

    contactos.add(contacto);
  }

  public void updateChatId(String username, long chatId) throws RepositoryInsertException {
    Optional<Telegram> contacto = get(username);

    if (contacto.isEmpty()) {
      throw new RepositoryInsertException("No existe un contacto con ese usuario");
    }

    contacto.get().setChatId(chatId);
  }

  public void deleteTodos() {
    contactos.clear();
  }
}
