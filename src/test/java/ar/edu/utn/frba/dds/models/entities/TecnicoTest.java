package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TecnicoTest {
  final CoordenadasGeograficas obelisco =
          new CoordenadasGeograficas(-34.603706013664166, -58.3815728218273);
  final CoordenadasGeograficas aCienMetrosDelObelisco =
          new CoordenadasGeograficas(-34.60375463775254, -58.38264297552039);
  final Heladera heladeraMock = mock(Heladera.class);

  @BeforeEach
  void setUp() {
    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
  }

  @Test
  void afirmaQueHeladeraEstaDentroDeRango() {
    assertTrue(
            new Tecnico(
                    new Documento(TipoDocumento.DNI, 1),
                    "",
                    "",
                    LocalDate.now(),
                    "123",
                    new AreaGeografica(aCienMetrosDelObelisco, 200f)).isDentroDeRango(heladeraMock)
    );
  }

  @Test
  void afirmaQueHeladeraNoEstaDentroDeRango() {
    assertFalse(
            new Tecnico(
                    new Documento(TipoDocumento.DNI, 1),
                    "",
                    "",
                    LocalDate.now(),
                    "123",
                    new AreaGeografica(aCienMetrosDelObelisco, 50f)).isDentroDeRango(heladeraMock)
    );
  }
}