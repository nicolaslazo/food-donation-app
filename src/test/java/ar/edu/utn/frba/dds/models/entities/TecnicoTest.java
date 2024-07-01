package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class TecnicoTest {
  final Ubicacion obelisco = new Ubicacion(-34.603706013664166, -58.3815728218273);
  final Ubicacion aCienMetrosDelObelisco = new Ubicacion(-34.60375463775254, -58.38264297552039);
  final Heladera heladeraMock = Mockito.mock(Heladera.class);

  @BeforeEach
  void setUp() {
    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
  }

  @Test
  void afirmaQueHeladeraEstaDentroDeRango() {
    assertTrue(
        new Tecnico("",
            "",
            new Documento(TipoDocumento.DNI, 123),
            "123",
            new Email("tecnico@example.com"),
            new AreaGeografica(aCienMetrosDelObelisco, 200f)
        ).isDentroDeRango(heladeraMock)
    );
  }

  @Test
  void afirmaQueHeladeraNoEstaDentroDeRango() {
    assertFalse(
        new Tecnico("",
            "",
            new Documento(TipoDocumento.DNI, 123),
            "123",
            new Email("tecnico@example.com"),
            new AreaGeografica(aCienMetrosDelObelisco, 50f)
        ).isDentroDeRango(heladeraMock)
    );
  }
}