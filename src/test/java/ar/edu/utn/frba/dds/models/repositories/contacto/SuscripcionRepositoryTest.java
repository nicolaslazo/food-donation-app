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
import org.mockito.MockedStatic;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SuscripcionRepositoryTest {
  final SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();
  final Heladera heladeraMock = mock(Heladera.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final Suscripcion suscripcion = new Suscripcion(heladeraMock, MotivoDeDistribucion.FALLA_HELADERA, null, colaboradorMock);

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
    Heladera mockHeladera = mock(Heladera.class);
    HeladerasRepository mockHeladerasRepository = mock(HeladerasRepository.class);

    // Configurando el mock para devolver 5 como capacidad de viandas
    when(mockHeladera.getCapacidadEnViandas()).thenReturn(5);
    // Configurando el mock para devolver 1 como cantidad de viandas depositadas
    when(mockHeladerasRepository.getCantidadViandasDepositadas(mockHeladera)).thenReturn(3);

    Suscripcion faltanViandasDeseada =
            new Suscripcion(mockHeladera, MotivoDeDistribucion.FALTAN_VIANDAS, 4, mock(Colaborador.class));
    Suscripcion faltanViandasIndeseada =
            new Suscripcion(mockHeladera, MotivoDeDistribucion.FALTAN_VIANDAS, 2, mock(Colaborador.class));
    Suscripcion faltaEspacioDeseada =
            new Suscripcion(mockHeladera, MotivoDeDistribucion.FALTA_ESPACIO, 4, mock(Colaborador.class));
    Suscripcion faltaEspacioIndeseada =
            new Suscripcion(mockHeladera, MotivoDeDistribucion.FALTA_ESPACIO, 2, mock(Colaborador.class));

    for (Suscripcion suscripcion :
            List.of(faltanViandasDeseada, faltanViandasIndeseada, faltaEspacioDeseada, faltaEspacioIndeseada)) {
      repositorio.insert(suscripcion);
    }

    Set<Suscripcion> interesadas;

    interesadas = repositorio.getInteresadasEnStock(mockHeladera, mockHeladerasRepository)
            .collect(Collectors.toSet());

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