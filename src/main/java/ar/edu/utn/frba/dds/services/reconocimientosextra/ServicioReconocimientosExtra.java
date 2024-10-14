package ar.edu.utn.frba.dds.services.reconocimientosextra;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ServicioReconocimientosExtra {
  static final String urlApi = ConfigLoader
      .getInstancia()
      .getProperty("servicios.reconocimientosExtraAPI.url");
  static ServicioReconocimientosExtra instancia = null;

  Retrofit retrofit;
  InterfazServicioReconocimientosExtra interfaz;

  private ServicioReconocimientosExtra() {
    retrofit = new Retrofit.Builder().baseUrl(urlApi).addConverterFactory(GsonConverterFactory.create()).build();
    interfaz = this.retrofit.create(InterfazServicioReconocimientosExtra.class);
  }

  public static ServicioReconocimientosExtra getInstancia() {
    if (instancia == null) instancia = new ServicioReconocimientosExtra();

    return instancia;
  }

  public ColeccionReporteReconocimientosColaborador solicitarRecomendaciones(int puntos, int minimoDonaciones, Integer maximo)
      throws IOException {
    Call<ColeccionReporteReconocimientosColaborador> request =
        interfaz.solicitarRecomendaciones(puntos, minimoDonaciones, maximo);
    Response<ColeccionReporteReconocimientosColaborador> response = request.execute();

    return response.body();
  }
}
