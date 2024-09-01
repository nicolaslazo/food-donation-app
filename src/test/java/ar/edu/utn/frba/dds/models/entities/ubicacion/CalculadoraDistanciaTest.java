package ar.edu.utn.frba.dds.models.entities.ubicacion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraDistanciaTest {
  @Test
  void testCalculaDistanciaEntreDosPuntos() {
    final CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.60373075523912, -58.381571117862336);
    final CoordenadasGeograficas bibliotecaNacional = new CoordenadasGeograficas(-34.584448384150896, -58.398029240191923);

    final int distanciaEnMetros = CalculadoraDistancia.calcular(obelisco, bibliotecaNacional).intValue();

    assertEquals(2620, distanciaEnMetros);
  }
}
