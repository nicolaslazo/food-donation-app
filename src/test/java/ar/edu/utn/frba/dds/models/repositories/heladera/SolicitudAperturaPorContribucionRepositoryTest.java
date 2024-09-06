package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
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


    repositorio.insert(solicitud);
    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(solicitud.getId(), false);

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSolicitudVieja() {
    ZonedDateTime ayer = ZonedDateTime.now().minusDays(1);
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ayer);
    repositorio.insert(solicitud);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(solicitud.getId(), false);

    assertFalse(encontrada.isPresent());
  }

  @Test
  void testInsertarSolicitud() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    repositorio.insert(solicitud);

    assertEquals(solicitud, repositorio.findById(solicitud.getId()).get());
  }

  @Test
  void testUpdateFechaUsadaFallaSiSolicitudVencio() {
    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now().minusYears(1));
    repositorio.insert(solicitud);

    assertThrows(SolicitudInvalidaException.class,
        () -> repositorio.updateFechaUsada(solicitud.getId(), false, ZonedDateTime.now()));
  }

  @Test
  void testUpdateFechaUsada() throws Exception {
    final ZonedDateTime haceUnAno = ZonedDateTime.now().minusYears(1);
    final ZonedDateTime haceUnAnoYDiezMinutos = haceUnAno.plusMinutes(10);

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, haceUnAno);
    repositorio.insert(solicitud);
    repositorio.updateFechaUsada(solicitud.getId(), false, haceUnAnoYDiezMinutos);

    assertEquals(haceUnAnoYDiezMinutos, repositorio.findById(solicitud.getId()).get().getFechaAperturaEnDestino());
  }

  @Test
  void testUpdateFechaUsadaFallaEnExtraccionDeDonacion() {
    /* Verifica que una excepción sea lanzada si se intenta marcar que una solicitud de apertura por donación
     * fue usada para una extracción de viandas para redistribución
     */

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());
    repositorio.insert(solicitud);
    assertThrows(SolicitudInvalidaException.class,
        () -> repositorio.updateFechaUsada(solicitud.getId(), true, ZonedDateTime.now()));
  }

  /* TODO: aveces funciona, aveces no. Cuando corre indivualmente funciona siempre.
  @Test
  void testUpdateFechaUsadaActualizaFechaDeExtraccionEnRedistribucion() throws Exception {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, mock(RedistribucionViandas.class), ZonedDateTime.now());
    int idSolicitud = repositorio.insert(solicitud);

    ZonedDateTime ahora = ZonedDateTime.now();
    repositorio.updateFechaUsada(solicitud.getId(), true, ahora);

    assertTrue(solicitud.isUsada(true));
    assertTrue(solicitud.isVigente(false));
    assertEquals(ahora, solicitud.getFechaAperturaEnOrigen());
  }
   */
  @Test
  void testEliminarTodas() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    repositorio.insert(solicitud);
    repositorio.deleteTodas();

    assertTrue(repositorio.getSolicitudVigente(solicitud.getId(), false).isEmpty());
  }
}