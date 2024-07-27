package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitudAperturaPorContribucionRepositoryTest {
  final Tarjeta tarjetaMock = Mockito.mock(Tarjeta.class);
  final DonacionViandas contribucionMock = Mockito.mock(DonacionViandas.class);
  final SolicitudAperturaPorContribucionRepository repositorio =
      SolicitudAperturaPorContribucionRepository.getInstancia();

  @AfterEach
  void tearDown() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetSolicitudVigente() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ZonedDateTime.now());

    int idSolicitud = repositorio.insert(solicitud);
    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(idSolicitud);

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSolicitudVieja() {
    ZonedDateTime ayer = ZonedDateTime.now().minusDays(1);
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ayer);
    int idSolicitud = repositorio.insert(solicitud);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(idSolicitud);

    assertFalse(encontrada.isPresent());
  }

  @Test
  void testInsertarSolicitud() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ZonedDateTime.now());

    int id = repositorio.insert(solicitud);

    assertEquals(1, id);
    assertEquals(1, solicitud.getId());
  }

  @Test
  void testUpdateFechaUsadaFallaSiSolicitudVencio() {
    final int idSolicitud = repositorio.insert(
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ZonedDateTime.now().minusYears(1)));

    assertThrows(SolicitudInvalidaException.class, () -> repositorio.updateFechaUsada(idSolicitud, ZonedDateTime.now()));
  }

  @Test
  void testUpdateFechaUsada() throws SolicitudInvalidaException {
    final ZonedDateTime haceUnAno = ZonedDateTime.now().minusYears(1);
    final ZonedDateTime haceUnAnoYDiezMinutos = haceUnAno.plusMinutes(10);

    final int idSolicitud =
        repositorio.insert(new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, haceUnAno));
    repositorio.updateFechaUsada(idSolicitud, haceUnAnoYDiezMinutos);

    assertEquals(haceUnAnoYDiezMinutos, repositorio.get(idSolicitud).get().getFechaUsada());
  }

  @Test
  void testEliminarTodas() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ZonedDateTime.now());

    int idSolicitud = repositorio.insert(solicitud);
    repositorio.deleteTodas();

    assertTrue(repositorio.getSolicitudVigente(idSolicitud).isEmpty());
  }
}