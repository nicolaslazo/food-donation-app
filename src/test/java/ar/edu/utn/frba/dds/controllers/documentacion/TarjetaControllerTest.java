package ar.edu.utn.frba.dds.controllers.documentacion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarjetaControllerTest {

  final TarjetasRepository tarjetasRepository = new TarjetasRepository();
  Tarjeta tarjeta;
  Usuario administrador;
  Usuario administradorInutil;
  Colaborador colaborador;

  @BeforeEach
  void setUp() throws RepositoryException, PermisoDenegadoException {
    new SeederRoles().seedRoles();

    administrador = new Usuario(
        new Documento(TipoDocumento.DNI, 321),
        "admin",
        "admin",
        LocalDate.now(),
        null,
        new HashSet<>(List.of(new RolesRepository().findByName("ADMINISTRADOR").get()))
    );
    administradorInutil = new Usuario(
        new Documento(TipoDocumento.DNI, 321),
        "admin",
        "admin",
        LocalDate.now(),
        null,
        new HashSet<>()
    );
    colaborador = new Colaborador(
        new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-34d, -58d),
        null,
        new RolesRepository().findByName("COLABORADORFISICO").get()
    );

    new UsuariosRepository().insert(administrador);
    new UsuariosRepository().insert(administradorInutil);
    new ColaboradorRepository().insert(colaborador);
    tarjeta = TarjetaController.crear(UUID.randomUUID(), administrador);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testCrearTarjetaAlmacenaEnRepositorio() throws RepositoryException, PermisoDenegadoException {
    UUID uuid = UUID.randomUUID();

    TarjetaController.crear(uuid, administrador);

    assertTrue(tarjetasRepository.findById(uuid).isPresent());
  }

  @Test
  void testCrearTarjetaSinPermisosFalla() {
    assertThrows(PermisoDenegadoException.class, () -> TarjetaController.crear(UUID.randomUUID(), administradorInutil));
  }

  @Test
  void testAltaDeTarjetaEsTrazable() throws PermisoDenegadoException {
    Optional<Tarjeta> tarjetaSeleccionada = tarjetasRepository.findById(tarjeta.getId());
    assertTrue(tarjetaSeleccionada.isPresent());

    TarjetaController.darDeAlta(tarjetaSeleccionada.get(), administrador, colaborador);
    Optional<Tarjeta> verificacion = tarjetasRepository.findById(tarjeta.getId());

    assertTrue(verificacion.isPresent());
    assertNotNull(verificacion.get().getFechaAlta());
    assertEquals(administrador, verificacion.get().getRecipiente());
    assertEquals(colaborador, verificacion.get().getProveedor());
  }

  @Test
  void testAltaFallaSiProveedorNoEsColaborador() {
    Colaborador colaboradorDummy = new Colaborador(
        new Documento(TipoDocumento.DNI, 2),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-34d, -58d),
        null,
        new RolesRepository().findByName("TECNICO").get()
    );

    assertThrows(PermisoDenegadoException.class,
        () -> TarjetaController.darDeAlta(tarjeta, administrador, colaboradorDummy));
  }

  @Test
  void testBajaDeTarjetaEsTrazable() throws PermisoDenegadoException {
    TarjetaController.darDeAlta(tarjeta, administrador, colaborador);
    TarjetaController.darDeBaja(tarjeta, administrador);
    Optional<Tarjeta> verificacion = tarjetasRepository.findById(tarjeta.getId());

    assertTrue(verificacion.isPresent());
    assertNotNull(verificacion.get().getFechaBaja());
    assertEquals(administrador, verificacion.get().getResponsableDeBaja());
  }

  @Test
  void testBajaFallaSiResponsableNoEsAdministrador() {
    assertThrows(PermisoDenegadoException.class, () -> TarjetaController.darDeBaja(tarjeta, administradorInutil));
  }
}