package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitudAperturaPorContribucionTest {
  final Tarjeta tarjetaMock = Mockito.mock(Tarjeta.class);
  final MovimientoViandas razonMock = Mockito.mock(MovimientoViandas.class);

  @Test
  void testNoSePuedeUsarSolicitudDosVeces() throws SolicitudInvalidaException {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now());

    solicitud.setFechaUsada(ZonedDateTime.now());

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testNoSePuedeUsarSolicitudVencida() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now().minusMonths(1));

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testNoSePuedeUsarSolicitudAntesDeSuVigencia() {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now().plusMinutes(10));

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testSolicitudUsadaSeDeclaraAsi() throws SolicitudInvalidaException {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now());

    solicitud.setFechaUsada(ZonedDateTime.now());

    assertTrue(solicitud.isUsada());
  }

  @Test
  void testSolicitudFrescaEstaVigente() {
    assertTrue(new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now()).isVigente());
  }

  @Test
  void testSolicitudUsadaNoEstaVigente() throws SolicitudInvalidaException {
    final SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now());

    solicitud.setFechaUsada(ZonedDateTime.now());

    assertFalse(solicitud.isVigente());
  }

  @Test
  void testSolicitudVencidaNoEstaVigente() {
    assertFalse(
        new SolicitudAperturaPorContribucion(tarjetaMock, razonMock, ZonedDateTime.now().minusMonths(1)).isVigente()
    );
  }
}