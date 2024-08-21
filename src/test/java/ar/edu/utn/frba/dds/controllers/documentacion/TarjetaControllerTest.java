package ar.edu.utn.frba.dds.controllers.documentacion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TarjetaControllerTest {
  Tarjeta tarjeta;
  Rol administrador = new Rol("Administrador",
      Set.of(new Permiso("crearTarjetas"),
          new Permiso("asignarTarjetas"),
          new Permiso("darBajaTarjetas")));
  Usuario usuario = new Usuario(mock(Email.class),
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      Set.of(administrador));
  Usuario usuarioInutil = new Usuario(mock(Email.class),
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new HashSet<>());
  Colaborador colaboradorMock = mock(Colaborador.class);

  @BeforeEach
  void setUp() {
    tarjeta = new Tarjeta(UUID.randomUUID());

    when(colaboradorMock.getUsuario()).thenReturn(usuario);
  }

  @AfterEach
  void tearDown() {
    TarjetasRepository.getInstancia().deleteTodas();
  }

  @Test
  void testCrearTarjetaAlmacenaEnRepositorio() throws RepositoryException, PermisoDenegadoException {
    UUID uuid = UUID.randomUUID();

    TarjetaController.crear(uuid, usuario);

    assertTrue(TarjetasRepository.getInstancia().get(uuid).isPresent());
  }

  @Test
  void testCrearTarjetaSinPermisosFalla() {
    assertThrows(PermisoDenegadoException.class, () -> TarjetaController.crear(UUID.randomUUID(), usuarioInutil));
  }

  @Test
  void testAltaDeTarjetaEsTrazable() throws PermisoDenegadoException {
    TarjetaController.darDeAlta(tarjeta, usuario, colaboradorMock);

    assertNotNull(tarjeta.getFechaAlta());
    assertEquals(usuario, tarjeta.getRecipiente());
    assertEquals(colaboradorMock, tarjeta.getProveedor());
  }

  @Test
  void testAltaFallaSiProveedorNoEsColaborador() {
    when(colaboradorMock.getUsuario()).thenReturn(usuarioInutil);

    assertThrows(PermisoDenegadoException.class,
        () -> TarjetaController.darDeAlta(tarjeta, usuario, colaboradorMock));
  }

  @Test
  void testAltaFallaSiRecipienteYaTieneTarjeta() throws PermisoDenegadoException, RepositoryException {
    Tarjeta unaTarjeta = TarjetaController.crear(UUID.randomUUID(), usuario);
    Tarjeta otraTarjeta = TarjetaController.crear(UUID.randomUUID(), usuario);

    TarjetaController.darDeAlta(unaTarjeta, usuario, colaboradorMock);

    assertThrows(PermisoDenegadoException.class,
        () -> TarjetaController.darDeAlta(otraTarjeta, usuario, colaboradorMock));
  }

  @Test
  void testBajaDeTarjetaEsTrazable() throws PermisoDenegadoException {
    TarjetaController.darDeAlta(tarjeta, usuario, colaboradorMock);
    TarjetaController.darDeBaja(tarjeta, usuario);

    assertNotNull(tarjeta.getFechaBaja());
    assertEquals(usuario, tarjeta.getResponsableDeBaja());
  }

  @Test
  void testBajaFallaSiResponsableNoEsAdministrador() {
    assertThrows(PermisoDenegadoException.class, () -> TarjetaController.darDeBaja(tarjeta, usuario));
  }
}