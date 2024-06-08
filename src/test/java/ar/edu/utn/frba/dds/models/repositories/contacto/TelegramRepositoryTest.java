package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TelegramRepositoryTest {
  final TelegramRepository repositorio = TelegramRepository.getInstancia();

  @BeforeEach
  void setUp() {
    repositorio.deleteTodos();
  }

  @Test
  void testGetPorUsuario() throws RepositoryInsertException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    Optional<Telegram> encontrado = repositorio.get("usuario");

    assertTrue(encontrado.isPresent());
    assertEquals("usuario", encontrado.get().getUsuario());
  }

  @Test
  void testInsertarContacto() throws RepositoryInsertException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    Optional<Telegram> encontrado = repositorio.get("usuario");

    assertTrue(encontrado.isPresent());
    assertEquals("usuario", encontrado.get().getUsuario());
  }

  @Test
  void testInsertarContactoDuplicadoLanzaExcepcion() throws RepositoryInsertException {
    Telegram unContacto = new Telegram("duplicado");
    Telegram otroContacto = new Telegram("duplicado");
    repositorio.insert(unContacto);

    assertThrows(RepositoryInsertException.class, () -> repositorio.insert(otroContacto));
  }

  @Test
  void testUpdateChatId() throws RepositoryInsertException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    repositorio.updateChatId("usuario", 123L);

    Telegram actualizado = repositorio.get("usuario").get();

    assertEquals(123L, actualizado.getChatId());
  }

  @Test
  void testActualizarChatIdUsuarioNoExistenteLanzaExcepcion() {
    assertThrows(RepositoryInsertException.class, () -> repositorio.updateChatId("noexiste", 67890L));
  }

  @Test
  void testEliminarTodos() throws RepositoryInsertException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    repositorio.deleteTodos();

    assertTrue(repositorio.get("usuario").isEmpty());
  }
}