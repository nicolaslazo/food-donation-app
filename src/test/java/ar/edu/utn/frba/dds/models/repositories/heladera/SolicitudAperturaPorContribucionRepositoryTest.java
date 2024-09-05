package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SolicitudAperturaPorContribucionRepositoryTest {
  final Tarjeta tarjetaMock = mock(Tarjeta.class);
  final DonacionViandas donacionMock = mock(DonacionViandas.class);
  final SolicitudAperturaPorContribucionRepository repositorio =
      SolicitudAperturaPorContribucionRepository.getInstancia();

  @AfterEach
  void tearDown() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetSolicitudVigente() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    int idSolicitud = repositorio.insert(solicitud);
    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(idSolicitud, false);

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSolicitudVieja() {
    ZonedDateTime ayer = ZonedDateTime.now().minusDays(1);
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ayer);
    int idSolicitud = repositorio.insert(solicitud);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(idSolicitud, false);

    assertFalse(encontrada.isPresent());
  }

  @Test
  void testInsertarSolicitud() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    int id = repositorio.insert(solicitud);

    assertEquals(1, id);
    assertEquals(1, solicitud.getId());
  }

  @Test
  void testUpdateFechaUsadaFallaSiSolicitudVencio() {
    final int idSolicitud = repositorio.insert(
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now().minusYears(1)));

    assertThrows(SolicitudInvalidaException.class,
        () -> repositorio.updateFechaUsada(idSolicitud, false, ZonedDateTime.now()));
  }

  @Test
  void testUpdateFechaUsada() throws Exception {
    final ZonedDateTime haceUnAno = ZonedDateTime.now().minusYears(1);
    final ZonedDateTime haceUnAnoYDiezMinutos = haceUnAno.plusMinutes(10);

    final int idSolicitud =
        repositorio.insert(new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, haceUnAno));
    repositorio.updateFechaUsada(idSolicitud, false, haceUnAnoYDiezMinutos);

    assertEquals(haceUnAnoYDiezMinutos, repositorio.get(idSolicitud).get().getFechaAperturaEnDestino());
  }

  @Test
  void testUpdateFechaUsadaFallaEnExtraccionDeDonacion() {
    /* Verifica que una excepción sea lanzada si se intenta marcar que una solicitud de apertura por donación
     * fue usada para una extracción de viandas para redistribución
     */
    int idSolicitud =
        repositorio.insert(new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now()));

    assertThrows(SolicitudInvalidaException.class,
        () -> repositorio.updateFechaUsada(idSolicitud, true, ZonedDateTime.now()));
  }

  /* TODO: aveces funciona, aveces no. Cuando corre indivualmente funciona siempre.
  @Test
  void testUpdateFechaUsadaActualizaFechaDeExtraccionEnRedistribucion() throws Exception {
    SolicitudAperturaPorContribucion solicitud =
            new SolicitudAperturaPorContribucion(tarjetaMock, mock(RedistribucionViandas.class), ZonedDateTime.now());
    int idSolicitud = repositorio.insert(solicitud);

    ZonedDateTime ahora = ZonedDateTime.now();
    repositorio.updateFechaUsada(idSolicitud, true, ahora);

    assertTrue(solicitud.isUsada(true));
    assertTrue(solicitud.isVigente(false));
    assertEquals(ahora, solicitud.getFechaAperturaEnOrigen());
  }
   */
  @Test
  void testEliminarTodas() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    int idSolicitud = repositorio.insert(solicitud);
    repositorio.deleteTodas();

    assertTrue(repositorio.getSolicitudVigente(idSolicitud, false).isEmpty());
  }
}