package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SolicitudAperturaPorContribucionTest {
  final Tarjeta tarjetaMock = mock(Tarjeta.class);
  final DonacionViandas donacionMock = mock(DonacionViandas.class);

  @Test
  void testNoSePuedeUsarSolicitudDosVeces() throws SolicitudInvalidaException {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    solicitud.setFechaAperturaEnDestino(ZonedDateTime.now());

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaAperturaEnDestino(ZonedDateTime.now()));
  }

  @Test
  void testNoSePuedeUsarSolicitudVencida() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now().minusMonths(1));

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaAperturaEnDestino(ZonedDateTime.now()));
  }

  @Test
  void testNoSePuedeUsarSolicitudAntesDeSuVigencia() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now().plusMinutes(10));

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaAperturaEnDestino(ZonedDateTime.now()));
  }

  @Test
  void testUsarSolicitudActualizaFechaContribucion()
      throws SolicitudInvalidaException {
    ZonedDateTime ahora = ZonedDateTime.now();

    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ahora.minusMinutes(1));

    solicitud.setFechaAperturaEnDestino(ahora);

    verify(donacionMock, times(1)).setFechaRealizada(ahora);
  }

  @Test
  void testSolicitudUsadaSeDeclaraAsi() throws SolicitudInvalidaException {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    solicitud.setFechaAperturaEnDestino(ZonedDateTime.now());

    assertTrue(solicitud.isUsada());
  }

  @Test
  void testSolicitudFrescaEstaVigente() {
    assertTrue(new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now()).isVigente());
  }

  @Test
  void testSolicitudUsadaNoEstaVigente() throws SolicitudInvalidaException {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    solicitud.setFechaAperturaEnDestino(ZonedDateTime.now());

    assertFalse(solicitud.isVigente());
  }

  @Test
  void testSolicitudVencidaNoEstaVigente() {
    assertFalse(
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now().minusMonths(1)).isVigente()
    );
  }

  @Test
  void testSolicitudPorDonacionNoPuedeSetearAperturaOrigen() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaAperturaEnOrigen(ZonedDateTime.now()));
  }

  @Test
  void testSolicitudNoPuedeAbrirOrigenDosVeces() throws SolicitudInvalidaException {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, mock(RedistribucionViandas.class), ZonedDateTime.now());

    solicitud.setFechaAperturaEnOrigen(ZonedDateTime.now());

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaAperturaEnOrigen(ZonedDateTime.now()));
  }
}