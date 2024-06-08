package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    Ubicacion obelisco = new Ubicacion(new Coordenadas(-34.6036152, -58.3817700));
    AreaGeografica alrededorDelObelisco = new AreaGeografica(obelisco, 50);

    ServicioSugerenciaColocacionHeladeras servicio = ServicioSugerenciaColocacionHeladeras.getInstancia();

    Retrofit retrofitMock = Mockito.mock(Retrofit.class);

    Field campoInstancia = ServicioSugerenciaColocacionHeladeras.class.getDeclaredField("retrofit");
    campoInstancia.setAccessible(true);
    campoInstancia.set(servicio, retrofitMock);

    InterfazServicioSugerenciaColocacionHeladeras mockInterfaz = mock(InterfazServicioSugerenciaColocacionHeladeras.class);
    when(retrofitMock.create(InterfazServicioSugerenciaColocacionHeladeras.class)).thenReturn(mockInterfaz);

    Call<List<Coordenadas>> mockCall = Mockito.mock(Call.class);
    when(mockInterfaz.sugerencias(-34.6036152, -58.3817700, 50.0)).thenReturn(mockCall);

    Response<List<Coordenadas>> mockResponse = Response.success(esperadas);
    when(mockCall.execute()).thenReturn(mockResponse);

    List<Coordenadas> sugerencias = servicio.solicitarSugerencias(alrededorDelObelisco);

    assertEquals(esperadas, sugerencias);
  }
}
