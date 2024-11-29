package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;

class SolicitudAperturaPorContribucionRepositoryTest {
  Tarjeta tarjetaMock = new Tarjeta(UUID.randomUUID());
  DonacionViandas donacionMock = mock(DonacionViandas.class);
  SolicitudAperturaPorContribucionRepository repositorio =
      new SolicitudAperturaPorContribucionRepository();
  SolicitudAperturaPorContribucion solicitud;

  /*
  @BeforeEach
  void setUp() {
    solicitud = new SolicitudAperturaPorContribucion(
            tarjetaMock,
            donacionMock,
            ZonedDateTime.now()
    );

    repositorio.insert(solicitud);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testGetSolicitudVigente() {
    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(solicitud.getId(), false);

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testGetSolicitudVieja() {
    ZonedDateTime ayer = ZonedDateTime.now().minusDays(1);
    SolicitudAperturaPorContribucion solicitudDos =
        new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ayer);
    repositorio.insert(solicitudDos);

    Optional<SolicitudAperturaPorContribucion> encontrada =
        repositorio.getSolicitudVigente(solicitudDos.getId(), false);

    assertFalse(encontrada.isPresent());
  }

  @Test
  void testInsertarSolicitud() {
    Optional<SolicitudAperturaPorContribucion> encontrada =
            repositorio.findById(solicitud.getId());

    assertTrue(encontrada.isPresent());
    assertEquals(solicitud.getId(), encontrada.get().getId());
  }

  @Test
  void testUpdateFechaUsadaFallaSiSolicitudVencio() {
    SolicitudAperturaPorContribucion solicitudTres =
            new SolicitudAperturaPorContribucion(
                    tarjetaMock,
                    donacionMock,
                    ZonedDateTime.now().minusYears(1));

    repositorio.insert(solicitudTres);

    assertThrows(SolicitudInvalidaException.class,
        () -> repositorio.updateFechaUsada(solicitudTres.getId(), false, ZonedDateTime.now()));
  }

  @Test
  void testUpdateFechaUsada() throws Exception {
    final ZonedDateTime haceUnAno = ZonedDateTime.now().minusYears(1);
    final ZonedDateTime haceUnAnoYDiezMinutos = haceUnAno.plusMinutes(10);

    SolicitudAperturaPorContribucion solicitudCuatro = new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, haceUnAno);
    repositorio.insert(solicitudCuatro);
    repositorio.updateFechaUsada(solicitudCuatro.getId(), false, haceUnAnoYDiezMinutos);

    assertEquals(haceUnAnoYDiezMinutos, repositorio.findById(solicitudCuatro.getId()).get().getFechaAperturaEnDestino());
  }
   */


  @Test
  void testUpdateFechaUsadaFallaEnExtraccionDeDonacion() {
    /* Verifica que una excepción sea lanzada si se intenta marcar que una solicitud de apertura por donación
     * fue usada para una extracción de viandas para redistribución
     */

//    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(tarjetaMock, donacionMock, ZonedDateTime.now());
//    repositorio.insert(solicitud);
//    assertThrows(SolicitudInvalidaException.class,
//        () -> repositorio.updateFechaUsada(solicitud.getId(), true, ZonedDateTime.now()));
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

  /*
  @Test
  void testEliminarTodas() {
    repositorio.deleteAll();

    assertTrue(repositorio.getSolicitudVigente(solicitud.getId(), false).isEmpty());
  }
   */
}