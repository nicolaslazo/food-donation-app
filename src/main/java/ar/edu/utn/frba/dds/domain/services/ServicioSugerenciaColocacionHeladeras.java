package ar.edu.utn.frba.dds.domain.services;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.domain.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.domain.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ServicioSugerenciaColocacionHeladeras {
  private static final String urlApi;
  private static ServicioSugerenciaColocacionHeladeras instancia = null;

  static {
    try {
      urlApi = new ConfigLoader("application.properties")
          .getProperty("servicios.sugerenciasUbicacionDeHeladerasAPI.url");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final Retrofit retrofit;

  private ServicioSugerenciaColocacionHeladeras() {
    retrofit = new Retrofit.Builder().baseUrl(urlApi).addConverterFactory(GsonConverterFactory.create()).build();
  }

  public static ServicioSugerenciaColocacionHeladeras getInstancia() {
    if (instancia == null) instancia = new ServicioSugerenciaColocacionHeladeras();

    return instancia;
  }

  public List<Ubicacion> solicitarSugerencias(AreaGeografica area) throws IOException {
    Coordenadas coordenadas = area.centro().coordenadas();

    InterfazServicioSugerenciaColocacionHeladeras interfaz =
        this.retrofit.create(InterfazServicioSugerenciaColocacionHeladeras.class);
    Call<List<Ubicacion>> request = interfaz.sugerencias(
        coordenadas.longitud(), coordenadas.latitud(), area.radioEnMetros()
    );
    Response<List<Ubicacion>> response = request.execute();
    @NonNull List<Ubicacion> sugerencias = response.body();

    return sugerencias;
  }
}
