package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

class SuscripcionRepositoryTest {
  final SuscripcionRepository repositorio = new SuscripcionRepository();
  final Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      null);
  final Heladera heladera = new Heladera("",
      new CoordenadasGeograficas(-34d, -58d),
      colaborador,
      5,
      ZonedDateTime.now(),
      "");
  final Suscripcion suscripcion =
      new Suscripcion(colaborador, heladera, MotivoDeDistribucion.FALLA_HELADERA, null);

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
    repositorio.insert(suscripcion);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testGetPorId() {
    Optional<Suscripcion> encontrada = repositorio.findById(suscripcion.getId());

    assertEquals(suscripcion.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSuscripcionesRelevantesAStockDeHeladera() {
    final Colaborador otroColaborador = new Colaborador(new Documento(TipoDocumento.DNI, 2),
        "",
        "",
        LocalDate.now(),
        null);
    final Colaborador otroColaboradorMas = new Colaborador(new Documento(TipoDocumento.DNI, 3),
        "",
        "",
        LocalDate.now(),
        null);
    final Colaborador todaviaOtroColaboradorMas = new Colaborador(new Documento(TipoDocumento.DNI, 4),
        "",
        "",
        LocalDate.now(),
        null);
    new ColaboradorRepository().insertAll(List.of(otroColaborador, otroColaboradorMas, todaviaOtroColaboradorMas));

    Suscripcion faltanViandasDeseada =
        new Suscripcion(colaborador, heladera, MotivoDeDistribucion.FALTAN_VIANDAS, 4);
    Suscripcion faltanViandasIndeseada =
        new Suscripcion(otroColaborador, heladera, MotivoDeDistribucion.FALTAN_VIANDAS, 2);
    Suscripcion faltaEspacioDeseada =
        new Suscripcion(otroColaboradorMas, heladera, MotivoDeDistribucion.FALTA_ESPACIO, 4);
    Suscripcion faltaEspacioIndeseada =
        new Suscripcion(todaviaOtroColaboradorMas, heladera, MotivoDeDistribucion.FALTA_ESPACIO, 2);

    repositorio.insertAll(List.of(faltanViandasDeseada,
        faltanViandasIndeseada,
        faltaEspacioDeseada,
        faltaEspacioIndeseada));

    Set<Suscripcion> interesadas;

    try (MockedConstruction<HeladerasRepository> ignored =
             mockConstruction(HeladerasRepository.class, (mock, context) ->
                 when(mock.getCantidadViandasDepositadas(heladera)).thenReturn(3L))) {
      interesadas = repositorio.findInteresadasEnStock(heladera).collect(Collectors.toSet());
    }

    assertEquals(Set.of(faltanViandasDeseada, faltaEspacioDeseada), interesadas);
  }

  @Test
  void testInsertarSuscripcion() {
    assertNotNull(suscripcion.getId());
  }

  @Test
  void testInsertarSuscripcionDuplicadaLanzaExcepcion() {
    repositorio.insert(new Suscripcion(colaborador, heladera, MotivoDeDistribucion.FALTAN_VIANDAS, 4));

    assertThrows(RuntimeException.class,
        () -> repositorio.insert(
            new Suscripcion(colaborador, heladera, MotivoDeDistribucion.FALTAN_VIANDAS, 6)));
  }

  @Test
  void testEliminarTodas() {
    repositorio.deleteAll();

    assertEquals(0, repositorio.findAll().count());
  }
}