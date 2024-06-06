package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.Suscripcion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuscripcionRepositoryTest {
  final SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();
  final Heladera heladeraMock = Mockito.mock(Heladera.class);
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final Suscripcion suscripcion = new Suscripcion(heladeraMock, colaboradorMock);

  @BeforeEach
  void setUp() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetPorId() throws RepositoryInsertException {
    repositorio.insert(suscripcion);

    Optional<Suscripcion> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testGetPorHeladera() throws RepositoryInsertException {
    final Colaborador otroColaboradorMock = Mockito.mock(Colaborador.class);
    repositorio.insert(suscripcion);
    repositorio.insert(new Suscripcion(heladeraMock, otroColaboradorMock));

    List<Suscripcion> suscripciones = repositorio.get(heladeraMock);

    assertEquals(2, suscripciones.size());
  }

  @Test
  void testGetPorHeladeraYColaborador() throws RepositoryInsertException {
    repositorio.insert(suscripcion);

    Optional<Suscripcion> encontrada = repositorio.get(heladeraMock, colaboradorMock);

    assertTrue(encontrada.isPresent());
    assertEquals(colaboradorMock, encontrada.get().getColaborador());
  }

  @Test
  void testInsertarSuscripcion() throws RepositoryInsertException {
    int id = repositorio.insert(suscripcion);

    assertEquals(1, id);
    assertEquals(1, suscripcion.getId());
  }

  @Test
  void testInsertarSuscripcionDuplicadaLanzaExcepcion() throws RepositoryInsertException {
    repositorio.insert(suscripcion);

    assertThrows(RepositoryInsertException.class, () -> repositorio.insert(new Suscripcion(heladeraMock, colaboradorMock)));
  }

  @Test
  void testEliminarTodas() throws RepositoryInsertException {
    repositorio.insert(suscripcion);

    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}