package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contacto.TipoNotificacion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
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
  final Suscripcion suscripcion = new Suscripcion(heladeraMock, TipoNotificacion.FALLA_HELADERA, colaboradorMock);

  @BeforeEach
  void setUp() throws RepositoryInsertException {
    repositorio.deleteTodas();

    repositorio.insert(suscripcion);
  }

  @Test
  void testGetPorId() {
    Optional<Suscripcion> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testGetPorHeladera() throws RepositoryInsertException {
    final Colaborador otroColaboradorMock = Mockito.mock(Colaborador.class);
    repositorio.insert(new Suscripcion(heladeraMock, TipoNotificacion.FALLA_HELADERA, otroColaboradorMock));

    List<Suscripcion> suscripciones = repositorio.get(heladeraMock);

    assertEquals(2, suscripciones.size());
  }

  @Test
  void testGetPorHeladeraYTipoDeNotificacion() {
    List<Suscripcion> encontradas = repositorio.get(heladeraMock, TipoNotificacion.FALLA_HELADERA);

    assertEquals(1, encontradas.size());
    assertEquals(suscripcion, encontradas.get(0));
  }

  @Test
  void testGetPorHeladeraYColaborador() {
    List<Suscripcion> encontradas = repositorio.get(heladeraMock, colaboradorMock);

    assertEquals(1, encontradas.size());
    assertEquals(colaboradorMock, encontradas.get(0).getColaborador());
  }

  @Test
  void testGetPorTodosLosAtributos() {
    Optional<Suscripcion> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(suscripcion, encontrada.get());
  }

  @Test
  void testInsertarSuscripcion() {
    assertEquals(1, suscripcion.getId());
  }

  @Test
  void testInsertarSuscripcionDuplicadaLanzaExcepcion() {
    assertThrows(RepositoryInsertException.class,
        () -> repositorio.insert(new Suscripcion(heladeraMock, TipoNotificacion.FALLA_HELADERA, colaboradorMock)));
  }

  @Test
  void testEliminarTodas() {
    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}