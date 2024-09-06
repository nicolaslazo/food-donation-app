package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;

public class TemperatureSensorCheckerTest {
  final CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);

  //Heladera en perfecto funcionamiento
  final Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaboradorMock,
      50,
      ZonedDateTime.now().minusMonths(5)
  );

  //Heladera defectuosa
  final Heladera otraHeladera = new Heladera("Otra heladera",
      new CoordenadasGeograficas(-34d, -58d),
      colaboradorMock,
      60,
      ZonedDateTime.now().minusMonths(7)
  );

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    //Esta heladera no falla, OtraHeladera si
    heladera.setUltimaTempRegistradaCelsius(0.0);

    //Esta heladera si falla
    Field campoMomentoUltimaTempRegistrada = Heladera.class.getDeclaredField("momentoUltimaTempRegistrada");
    campoMomentoUltimaTempRegistrada.setAccessible(true);
    campoMomentoUltimaTempRegistrada.set(otraHeladera, ZonedDateTime.now().minusMinutes(7));

    HeladerasRepository repositorio = new HeladerasRepository();
    repositorio.insert(heladera);
    repositorio.insert(otraHeladera);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  // Tests inhabilitados por https://github.com/dds-utn/2024-tpa-ma-ma-grupo-06/issues/157
  //  @Test
  //  void testTemperatureSensorChecker() throws CheckerException {
  //    // Ejecutamos el método a probar
  //    TemperatureSensorChecker.main(new String[] {});
  //
  //    // Verificamos que se haya llamado al método cargarIncidente para la heladera desactualizada
  //    ArgumentCaptor<TipoIncidente> tipoIncidenteCaptor = ArgumentCaptor.forClass(TipoIncidente.class);
  //    ArgumentCaptor<Heladera> heladeraCaptor = ArgumentCaptor.forClass(Heladera.class);
  //
  //    verify(cargadorIncidenteMock).cargarIncidente(tipoIncidenteCaptor.capture(), heladeraCaptor.capture());
  //
  //    assertEquals(TipoIncidente.FALLA_CONEXION, tipoIncidenteCaptor.getValue());
  //    assertEquals(otraHeladera, heladeraCaptor.getValue());
  //  }
  //
  //  @Test
  //  void testTemperatureSensorCheckerThrowsException() throws CheckerException {
  //    repositoryHeladeras.deleteTodas();
  //
  //    TemperatureSensorChecker temperatureSensorChecker = new TemperatureSensorChecker(cargadorIncidenteMock);
  //
  //    CheckerException exception = assertThrows(CheckerException.class, () -> {
  //      temperatureSensorChecker.main(null);
  //    });
  //  }
}
