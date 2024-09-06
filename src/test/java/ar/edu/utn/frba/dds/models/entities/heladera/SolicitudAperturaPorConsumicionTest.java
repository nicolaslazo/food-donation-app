package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SolicitudAperturaPorConsumicionTest {
  final Tarjeta tarjetaMock = mock(Tarjeta.class);
  final Vianda viandaMock = mock(Vianda.class);

  @Test
  void testNoSePuedeUsarSolicitudDosVeces() throws SolicitudInvalidaException {
    final SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ZonedDateTime.now());

    solicitud.setFechaUsada(ZonedDateTime.now());

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testNoSePuedeUsarSolicitudVencida() {
    final SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ZonedDateTime.now().minusMonths(1));

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testNoSePuedeUsarSolicitudAntesDeSuVigencia() {
    final SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ZonedDateTime.now().plusMinutes(10));

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testUsarSolicitudActualizaUbicacionDeVianda() throws SolicitudInvalidaException {
    ZonedDateTime ahora = ZonedDateTime.now();

    SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ahora.minusMinutes(1));

    solicitud.setFechaUsada(ahora);

    verify(viandaMock, times(1)).setHeladera(null);
  }

  @Test
  void testSolicitudUsadaSeDeclaraAsi() throws SolicitudInvalidaException {
    SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ZonedDateTime.now());

    assertNull(solicitud.fechaUsada);
    solicitud.setFechaUsada(ZonedDateTime.now());
    assertNotNull(solicitud.fechaUsada);
  }

  @Test
  void testSolicitudFrescaNoEstaUsada() {
    SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ZonedDateTime.now());

    assertNull(solicitud.fechaUsada);
  }

  @Test
  void testSolicitudUsadaNoSePuedeUsarNuevamente() throws SolicitudInvalidaException {
    final SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, ZonedDateTime.now());

    solicitud.setFechaUsada(ZonedDateTime.now());

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }

  @Test
  void testSolicitudVencidaNoSePuedeUsar() {
    ZonedDateTime fechaCreacion = ZonedDateTime.now().minusMonths(1);
    SolicitudAperturaPorConsumicion solicitud =
        new SolicitudAperturaPorConsumicion(tarjetaMock, viandaMock, fechaCreacion);

    assertThrows(SolicitudInvalidaException.class, () -> solicitud.setFechaUsada(ZonedDateTime.now()));
  }
}

