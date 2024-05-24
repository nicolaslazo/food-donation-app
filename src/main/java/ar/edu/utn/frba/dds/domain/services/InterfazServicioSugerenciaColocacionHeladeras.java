package ar.edu.utn.frba.dds.domain.services;

import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface InterfazServicioSugerenciaColocacionHeladeras {
  @GET("sugerencias")
  Call<List<Ubicacion>> sugerencias(
      @Query("longitud") double longitud,
      @Query("latitud") double latitud,
      @Query("radioAreaEnMetros") float radioAreaEnMetros
  );
}
