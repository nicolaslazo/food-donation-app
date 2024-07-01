package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuscripcionRepositoryTest {
  final SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();
  final Heladera heladeraMock = Mockito.mock(Heladera.class);
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
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