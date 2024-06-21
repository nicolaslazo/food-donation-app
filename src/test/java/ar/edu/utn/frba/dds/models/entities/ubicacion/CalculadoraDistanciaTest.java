package ar.edu.utn.frba.dds.models.entities.ubicacion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraDistanciaTest {
  @Test
  void testCalculaDistanciaEntreDosPuntos() {
    final Coordenadas obelisco = new Coordenadas(-34.60373075523912, -58.381571117862336);
    final Coordenadas bibliotecaNacional = new Coordenadas(-34.584448384150896, -58.398029240191923);

    final int distanciaEnMetros = (int) CalculadoraDistancia.calcular(obelisco, bibliotecaNacional);

    assertEquals(2620, distanciaEnMetros);
  }
}
