package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class HeladeraControllerTest {
  final TecnicoRepository tecnicoRepository = TecnicoRepository.getInstancia();
  final HeladeraController heladeraController = new HeladeraController();
  final Heladera heladeraMock = Mockito.mock(Heladera.class);
  final Ubicacion obelisco = new Ubicacion(-34.603706013664166, -58.3815728218273);
  final Ubicacion aCienMetrosDelObelisco = new Ubicacion(-34.60375463775254, -58.38264297552039);

  @BeforeEach
  void setUp() {
    tecnicoRepository.deleteTodos();

    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
  }

  @Test
  void testDevuelveNadaSiNoEncuentraTecnicosProximos() {
    assertTrue(heladeraController.encontrarTecnicoMasCercano(heladeraMock).isEmpty());
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
    assertTrue(heladeraController.encontrarTecnicoMasCercano(heladeraMock).isEmpty());
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

    assertEquals(Optional.of(tecnicoDeseado), heladeraController.encontrarTecnicoMasCercano(heladeraMock));
  }
}