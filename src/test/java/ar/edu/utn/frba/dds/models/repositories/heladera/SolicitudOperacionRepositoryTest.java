package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitudOperacionRepositoryTest {
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final Heladera heladeraMock = Mockito.mock(Heladera.class);
  final DonacionViandas contribucionMock = Mockito.mock(DonacionViandas.class);
  final SolicitudOperacionRepository repositorio = SolicitudOperacionRepository.getInstancia();

  @BeforeEach
  void setUp() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetSolicitudVigente() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(colaboradorMock, heladeraMock, contribucionMock, ZonedDateTime.now());
    repositorio.insert(solicitud);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(colaboradorMock, heladeraMock);

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSolicitudVieja() {
    ZonedDateTime ayer = ZonedDateTime.now().minusDays(1);
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(colaboradorMock, heladeraMock, contribucionMock, ayer);
    repositorio.insert(solicitud);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(colaboradorMock, heladeraMock);

    assertFalse(encontrada.isPresent());
  }

  @Test
  void testInsertarSolicitud() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(colaboradorMock, heladeraMock, contribucionMock, ZonedDateTime.now());

    int id = repositorio.insert(solicitud);

    assertEquals(1, id);
    assertEquals(1, solicitud.getId());
  }

  @Test
  void testEliminarTodas() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(colaboradorMock, heladeraMock, contribucionMock, ZonedDateTime.now());

    repositorio.insert(solicitud);
    repositorio.deleteTodas();

    assertTrue(repositorio.getSolicitudVigente(solicitud.getColaborador(), solicitud.getHeladera()).isEmpty());
  }
}