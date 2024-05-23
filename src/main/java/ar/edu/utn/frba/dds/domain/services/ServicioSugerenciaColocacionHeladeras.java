package ar.edu.utn.frba.dds.domain.services;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

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

  public ServicioSugerenciaColocacionHeladeras getInstancia() {
    if (instancia == null) instancia = new ServicioSugerenciaColocacionHeladeras();

    return instancia;
  }
}
