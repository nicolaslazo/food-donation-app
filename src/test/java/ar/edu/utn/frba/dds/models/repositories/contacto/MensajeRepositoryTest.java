package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.contacto.Mensaje;
import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;

public class MensajeRepositoryTest {

  MensajeRepository repositorio = new MensajeRepository();

  Usuario usuario = new Usuario(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      null,
      new HashSet<>());

  Telegram contacto = new Telegram(usuario, "usuario");

  Mensaje mensaje = new Mensaje(contacto, "holaa", ZonedDateTime.now());

  @BeforeEach
  void setUp() {
    new UsuariosRepository().insert(usuario);
    new ContactosRepository().insert(contacto);
    repositorio.insert(mensaje);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }
  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(MensajeRepository.class, repositorio);
  }

  @Test
  void testInsert() {
    Assertions.assertNotNull(mensaje.getId());
  }
}
