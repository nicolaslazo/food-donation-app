package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
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
  void testGetPorUsuario() throws RepositoryException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    Optional<Telegram> encontrado = repositorio.get("usuario");

    assertTrue(encontrado.isPresent());
    assertEquals("usuario", encontrado.get().getUsuario());
  }

  @Test
  void testInsertarContacto() throws RepositoryException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    Optional<Telegram> encontrado = repositorio.get("usuario");

    assertTrue(encontrado.isPresent());
    assertEquals("usuario", encontrado.get().getUsuario());
  }

  @Test
  void testInsertarContactoDuplicadoLanzaExcepcion() throws RepositoryException {
    Telegram unContacto = new Telegram("duplicado");
    Telegram otroContacto = new Telegram("duplicado");
    repositorio.insert(unContacto);

    assertThrows(RepositoryException.class, () -> repositorio.insert(otroContacto));
  }

  @Test
  void testUpdateChatId() throws RepositoryException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    repositorio.updateChatId("usuario", 123L);

    Telegram actualizado = repositorio.get("usuario").get();

    assertEquals(123L, actualizado.getChatId());
  }

  @Test
  void testActualizarChatIdUsuarioNoExistenteLanzaExcepcion() {
    assertThrows(RepositoryException.class, () -> repositorio.updateChatId("noexiste", 67890L));
  }

  @Test
  void testEliminarTodos() throws RepositoryException {
    Telegram contacto = new Telegram("usuario");
    repositorio.insert(contacto);

    repositorio.deleteTodos();

    assertTrue(repositorio.get("usuario").isEmpty());
  }
}