package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeladeraControllerTest {
  final TecnicoRepository tecnicoRepository = TecnicoRepository.getInstancia();
  final Heladera heladeraMock = Mockito.mock(Heladera.class);
  final Ubicacion obelisco = new Ubicacion(-34.603706013664166, -58.3815728218273);
  final Ubicacion aCienMetrosDelObelisco = new Ubicacion(-34.60375463775254, -58.38264297552039);

  @BeforeEach
  void setUp() {
    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
  }

  @AfterEach
  void tearDown() {
    tecnicoRepository.deleteTodos();
  }

  @Test
  void testDevuelveNadaSiNoEncuentraTecnicosProximos() {
    assertTrue(HeladeraController.encontrarTecnicoMasCercano(heladeraMock).isEmpty());
  }

  @Test
  void testDevuelveNadaSiLosTecnicosProximosNoLleganEnRango() {
    tecnicoRepository.insertTecnico(
        new Tecnico("",
            "",
            new Documento(TipoDocumento.DNI, 123),
            "123",
            new Email("tecnico@example.com"),
            new AreaGeografica(aCienMetrosDelObelisco, 50f))
    );
    assertTrue(HeladeraController.encontrarTecnicoMasCercano(heladeraMock).isEmpty());
  }

  @Test
  void testPriorizaTecnicoMasCercanoEnCasoDeVariosDisponibles() {
    final Ubicacion aCincuentaMetrosDelObelisco = new Ubicacion(-34.603725171426916,
        -58.38211380719743);
    Tecnico tecnicoDeseado = new Tecnico("",
        "",
        new Documento(TipoDocumento.DNI, 123),
        "123",
        new Email("tecnico@example.com"),
        new AreaGeografica(aCincuentaMetrosDelObelisco, 1000f));

    tecnicoRepository.insertTecnico(tecnicoDeseado);
    tecnicoRepository.insertTecnico(
        new Tecnico("",
            "",
            new Documento(TipoDocumento.DNI, 456),
            "456",
            new Email("tecnico@example.com"),
            new AreaGeografica(aCienMetrosDelObelisco, 1000f))
    );

    assertEquals(Optional.of(tecnicoDeseado), HeladeraController.encontrarTecnicoMasCercano(heladeraMock));
  }

  @Test
  void testEncuentraHeladerasCercanas() throws RepositoryException {
    final List<Heladera> heladeras = new ArrayList<>(5);

    for (int i = 0; i < 5; i++) {
      heladeras.add(new Heladera("",
          new Ubicacion(-34, -58 - i),
          mock(Colaborador.class),
          ZonedDateTime.now(),
          10,
          0,
          10,
          5));
    }

    final Heladera heladeraTarget = heladeras.get(0);

    Collections.shuffle(heladeras);

    for (Heladera heladera : heladeras) HeladerasRepository.getInstancia().insert(heladera);

    final List<Heladera> sugerencias = new HeladeraController().encontrarHeladerasCercanas(heladeraTarget);
    final int[] longitudes =
        sugerencias.stream().mapToInt(sugerencia -> (int) sugerencia.getUbicacion().getLongitud()).toArray();

    assertEquals(-59, longitudes[0]);
    assertEquals(-60, longitudes[1]);
    assertEquals(-61, longitudes[2]);
  }
}