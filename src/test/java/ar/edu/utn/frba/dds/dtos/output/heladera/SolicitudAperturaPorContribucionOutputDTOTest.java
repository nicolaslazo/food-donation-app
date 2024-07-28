package ar.edu.utn.frba.dds.dtos.output.heladera;

import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolicitudAperturaPorContribucionOutputDTOTest {
  @BeforeEach
  void setUp() {
    SolicitudAperturaPorContribucionRepository.getInstancia().deleteTodas();
  }

  @Test
  void testGeneraJson() {
    Tarjeta tarjetaMock = Mockito.mock(Tarjeta.class);
    SolicitudAperturaPorContribucion solicitudMock = Mockito.mock(SolicitudAperturaPorContribucion.class);

    Mockito.when(tarjetaMock.getId()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    Mockito.when(solicitudMock.getTarjeta()).thenReturn(tarjetaMock);
    Mockito.when(solicitudMock.getFechaVencimiento()).thenReturn(ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
    Mockito.when(solicitudMock.getPesosDeViandasEnGramos()).thenReturn(new double[]{1, 10, 100});

    assertEquals(
        "{\"identificadorTarjeta\":\"00000000-0000-0000-0000-000000000000\"," +
            "\"idSolicitud\":0," +
            "\"fechaVencimientoSerializadaIso8601\":\"1970-01-01T00:00Z\"," +
            "\"pesosViandasEnGramos\":[1.0,10.0,100.0]}",
        new SolicitudAperturaPorContribucionOutputDTO(solicitudMock).enJson());
  }
}