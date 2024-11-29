package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.ContribucionYaRealizadaException;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntregaTarjetasRepositoryTest {
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  Colaborador colaborador = new Colaborador(
          new Documento(TipoDocumento.DNI, 1),
          "",
          "",
          LocalDate.now(),
          obelisco);
  Usuario usuario = new Usuario(
          new Documento(TipoDocumento.DNI, 1),
          "",
          "",
          LocalDate.now(),
          null,
          new HashSet<>());
  Tarjeta tarjeta = new Tarjeta(UUID.randomUUID());
  Tarjeta tarjetaDos = new Tarjeta(UUID.randomUUID());
  EntregaTarjetas entrega =
      new EntregaTarjetas(colaborador, Collections.singletonList(tarjeta));
  EntregaTarjetas otraEntrega =
      new EntregaTarjetas(colaborador, Collections.singletonList(tarjetaDos));

  @BeforeEach
  void setUp() throws ContribucionYaRealizadaException {
    new ColaboradorRepository().insert(colaborador);
    new UsuariosRepository().insert(usuario);
    entrega.setFechaRealizada(ZonedDateTime.now());
    new TarjetasRepository().insertAll(List.of(tarjeta, tarjetaDos));
    new EntregaTarjetasRepository().insert(entrega);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testGetPorId() {
    Optional<EntregaTarjetas> encontrada = new EntregaTarjetasRepository().findById(entrega.getId());

    assertTrue(encontrada.isPresent());
    assertEquals(entrega.getId(), encontrada.get().getId());
  }

  // TODO: Tecnicamente no hace el insert, ya contempla el unique = true. Pero no lanza exception
  //  @Test
  //  void testInsertarEntregaConTarjetasRepetidasLanzaExcepcion() throws ContribucionYaRealizadaException {
  //    EntregaTarjetas entregaCopiona = new EntregaTarjetas(colaborador, Collections.singletonList(tarjeta));
  //    entregaCopiona.setFechaRealizada(ZonedDateTime.now());
  //    assertThrows(RollbackException.class, () ->
  //      new EntregaTarjetasRepository().insert(entregaCopiona));
  //  }

  @Test
  void testEliminarTodo() {
    new EntregaTarjetasRepository().insert(otraEntrega);
    new TarjetasRepository().deleteAll();
    new EntregaTarjetasRepository().deleteAll();

    Stream<EntregaTarjetas> vacio = new EntregaTarjetasRepository().findAll();

    assertEquals(0,vacio.count());
  }
}