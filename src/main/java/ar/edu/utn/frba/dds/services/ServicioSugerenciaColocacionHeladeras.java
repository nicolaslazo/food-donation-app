package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Coordenadas;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ServicioSugerenciaColocacionHeladeras {
  static final String urlApi = ConfigLoader
      .getInstancia()
      .getProperty("servicios.sugerenciasUbicacionDeHeladerasAPI.url");
  static ServicioSugerenciaColocacionHeladeras instancia = null;

  Retrofit retrofit;
  InterfazServicioSugerenciaColocacionHeladeras interfaz;

  private ServicioSugerenciaColocacionHeladeras() {
    retrofit = new Retrofit.Builder().baseUrl(urlApi).addConverterFactory(GsonConverterFactory.create()).build();
    interfaz = this.retrofit.create(InterfazServicioSugerenciaColocacionHeladeras.class);
  }

  public static ServicioSugerenciaColocacionHeladeras getInstancia() {
    if (instancia == null) instancia = new ServicioSugerenciaColocacionHeladeras();

    return instancia;
  }

  public static void main(String[] args) {
    // Demo usando una mock API de Postman
    Coordenadas obelisco = new Coordenadas(-34.6036152, -58.381700);
    AreaGeografica alrededorDelObelisco = new AreaGeografica(obelisco, 50);

    ServicioSugerenciaColocacionHeladeras servicio = ServicioSugerenciaColocacionHeladeras.getInstancia();
    try {
      List<Coordenadas> sugerencias = servicio.solicitarSugerencias(alrededorDelObelisco);

      System.out.print(sugerencias);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Coordenadas> solicitarSugerencias(AreaGeografica area) throws IOException {
    Coordenadas coordenadas = area.centro();

    Call<List<Coordenadas>> request =
        interfaz.sugerencias(coordenadas.getLatitud(), coordenadas.getLongitud(), area.radioEnMetros());

    Response<List<Coordenadas>> response = request.execute();

    return response.body();
  }
}
