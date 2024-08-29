package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

class ContactosRepositoryTest {
  final ContactosRepository repositorio = ContactosRepository.getInstancia();
  final UsuariosRepository repositorioUsuarios =
      UsuariosRepository.getInstancia();


  Usuario usuario = new Usuario(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new HashSet<>());
  Usuario usuarioMock = spy(usuario);

  @BeforeEach
  void setUp() {
    repositorioUsuarios.insert(usuario);
  }

  @AfterEach
  void tearDown() {
    repositorio.deleteAll();
    repositorioUsuarios.deleteAll();
  }

  @Test
  void testGetPorUsuarioDeTelegram() {
    Telegram contacto = new Telegram(usuarioMock, "usuario");
    repositorio.insert(contacto);

    Optional<Telegram> encontrado = repositorio.get(
        "usuario",
        Telegram.class
    );

    assertTrue(encontrado.isPresent());
    assertEquals("usuario", encontrado.get().getDestinatario());
  }

  @Test
  void testInsertarContacto() {
    Telegram contacto = new Telegram(usuarioMock, "usuario");
    repositorio.insert(contacto);

    Optional<Telegram> encontrado = repositorio.get(
        "usuario",
        Telegram.class
    );

    assertTrue(encontrado.isPresent());
    assertEquals("usuario", encontrado.get().getDestinatario());
  }

  @Test
  void testInsertarContactoDuplicadoLanzaExcepcion() {
    Telegram unContacto = new Telegram(usuarioMock, "duplicado");
    Telegram otroContacto = new Telegram(usuarioMock, "duplicado");
    repositorio.insert(unContacto);

    assertThrows(RollbackException.class, () -> repositorio.insert(otroContacto));
  }

  @Test
  void testUpdateChatId() throws RepositoryException {
    Telegram contacto = new Telegram(usuarioMock, "usuario");

    repositorio.insert(contacto);
    repositorio.updateChatId("usuario", 123L);

    Telegram actualizado = repositorio.get("usuario", Telegram.class).get();

    assertEquals(123L, actualizado.getChatId());
  }

  @Test
  void testActualizarChatIdUsuarioNoExistenteLanzaExcepcion() {
    assertThrows(RepositoryException.class, () ->
        repositorio.updateChatId("noexiste", 67890L)
    );
  }

  @Test
  void testEliminarTodos() {
    Telegram contacto = new Telegram(usuarioMock, "usuario");
    repositorio.insert(contacto);

    repositorio.deleteAll();

    assertTrue(repositorio.get("usuario", Telegram.class).isEmpty());
  }
}
