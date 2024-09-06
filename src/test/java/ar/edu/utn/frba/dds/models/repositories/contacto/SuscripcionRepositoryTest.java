package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

class SuscripcionRepositoryTest {
  final SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();
  final Heladera heladeraMock = mock(Heladera.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final Suscripcion suscripcion =
      new Suscripcion(heladeraMock, MotivoDeDistribucion.FALLA_HELADERA, null, colaboradorMock);

  @BeforeEach
  void setUp() throws RepositoryException {
    repositorio.insert(suscripcion);
  }

  @AfterEach
  void tearDown() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetPorId() {
    Optional<Suscripcion> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testGetSuscripcionesRelevantesAStockDeHeladera() throws RepositoryException {
    Heladera heladeraMock = mock(Heladera.class);
    when(heladeraMock.getCapacidadEnViandas()).thenReturn(5);

    Suscripcion faltanViandasDeseada =
        new Suscripcion(heladeraMock, MotivoDeDistribucion.FALTAN_VIANDAS, 4, mock(Colaborador.class));
    Suscripcion faltanViandasIndeseada =
        new Suscripcion(heladeraMock, MotivoDeDistribucion.FALTAN_VIANDAS, 2, mock(Colaborador.class));
    Suscripcion faltaEspacioDeseada =
        new Suscripcion(heladeraMock, MotivoDeDistribucion.FALTA_ESPACIO, 4, mock(Colaborador.class));
    Suscripcion faltaEspacioIndeseada =
        new Suscripcion(heladeraMock, MotivoDeDistribucion.FALTA_ESPACIO, 2, mock(Colaborador.class));

    for (Suscripcion suscripcion :
        List.of(faltanViandasDeseada, faltanViandasIndeseada, faltaEspacioDeseada, faltaEspacioIndeseada)) {
      repositorio.insert(suscripcion);
    }

    Set<Suscripcion> interesadas;

    try (MockedConstruction<HeladerasRepository> ignored =
             mockConstruction(HeladerasRepository.class, (mock, context) ->
                 when(mock.getCantidadViandasDepositadas(heladeraMock)).thenReturn(3L))) {
      interesadas = repositorio.getInteresadasEnStock(heladeraMock).collect(Collectors.toSet());
    }

    assertEquals(Set.of(faltanViandasDeseada, faltaEspacioDeseada), interesadas);
  }

  @Test
  void testInsertarSuscripcion() {
    assertEquals(1, suscripcion.getId());
  }

  @Test
  void testInsertarSuscripcionDuplicadaLanzaExcepcion() throws RepositoryException {
    repositorio.insert(new Suscripcion(heladeraMock, MotivoDeDistribucion.FALTAN_VIANDAS, 4, colaboradorMock));

    assertThrows(RepositoryException.class,
        () -> repositorio.insert(
            new Suscripcion(heladeraMock, MotivoDeDistribucion.FALTAN_VIANDAS, 6, colaboradorMock)));
  }

  @Test
  void testEliminarTodas() {
    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}