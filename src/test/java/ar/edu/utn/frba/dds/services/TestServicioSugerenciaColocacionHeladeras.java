package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Coordenadas;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestServicioSugerenciaColocacionHeladeras {
  @Test
  public void testPuedeSolicitarSugerencias() throws IOException, NoSuchFieldException, IllegalAccessException {
    List<Coordenadas> esperadas = Arrays.asList(
        new Coordenadas(-34.6036150, -58.381700),
        new Coordenadas(-34.6036200, -58.381750)
    );

    Coordenadas obelisco = new Coordenadas(-34.6036152, -58.3817700);
    AreaGeografica alrededorDelObelisco = new AreaGeografica(obelisco, 50);

    ServicioSugerenciaColocacionHeladeras servicio = ServicioSugerenciaColocacionHeladeras.getInstancia();

    Call<List<Coordenadas>> mockCall = mock(Call.class);
    when(mockCall.execute()).thenReturn(Response.success(esperadas));

    InterfazServicioSugerenciaColocacionHeladeras interfazMock =
        mock(InterfazServicioSugerenciaColocacionHeladeras.class);
    when(interfazMock.sugerencias(-34.6036152, -58.3817700, 50.0)).thenReturn(mockCall);

    Field campoInterfaz = ServicioSugerenciaColocacionHeladeras.class.getDeclaredField("interfaz");
    campoInterfaz.setAccessible(true);
    campoInterfaz.set(servicio, interfazMock);

    List<Coordenadas> sugerencias = servicio.solicitarSugerencias(alrededorDelObelisco);

    assertEquals(esperadas, sugerencias);
  }
}

