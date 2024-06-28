package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitudAperturaPorContribucionRepositoryTest {
  final Tarjeta tarjetaMock = Mockito.mock(Tarjeta.class);
  final DonacionViandas contribucionMock = Mockito.mock(DonacionViandas.class);
  final SolicitudAperturaPorContribucionRepository repositorio = SolicitudAperturaPorContribucionRepository.getInstancia();

  @BeforeEach
  void setUp() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetSolicitudVigente() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ZonedDateTime.now());

    repositorio.insert(solicitud);
    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(tarjetaMock, contribucionMock);

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSolicitudVieja() {
    ZonedDateTime ayer = ZonedDateTime.now().minusDays(1);
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ayer);
    repositorio.insert(solicitud);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(tarjetaMock, contribucionMock);

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
  void testEliminarTodas() {
    SolicitudAperturaPorContribucion solicitud =
        new SolicitudAperturaPorContribucion(tarjetaMock, contribucionMock, ZonedDateTime.now());

    repositorio.insert(solicitud);
    repositorio.deleteTodas();

    assertTrue(repositorio.getSolicitudVigente(solicitud.getTarjeta(), solicitud.getRazon()).isEmpty());
  }
}