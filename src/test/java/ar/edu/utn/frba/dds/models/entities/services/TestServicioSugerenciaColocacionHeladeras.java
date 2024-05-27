package ar.edu.utn.frba.dds.models.entities.services;

import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TestServicioSugerenciaColocacionHeladeras {
  private static ServicioSugerenciaColocacionHeladeras servicio;

  @BeforeAll
  static void setUp() {
    servicio = ServicioSugerenciaColocacionHeladeras.getInstancia();
  }

  @Test
  public void testSeInicializaServicio() {
    assertInstanceOf(ServicioSugerenciaColocacionHeladeras.class, servicio);
  }

  @Test
  public void testPuedeSolicitarSugerencias() throws IOException {
    Ubicacion obelisco = new Ubicacion(new Coordenadas(-34.6036152, -58.381700));
    AreaGeografica alrededorDelObelisco = new AreaGeografica(obelisco, 50);

    List<Coordenadas> esperadas = Arrays.asList(
        new Coordenadas(-34.6036150, -58.381700),
        new Coordenadas(-34.6036200, -58.381750)
    );

    servicio = ServicioSugerenciaColocacionHeladeras.getInstancia();
    List<Coordenadas> sugerencias = servicio.solicitarSugerencias(alrededorDelObelisco);

    assertEquals(esperadas, sugerencias);
  }
}
