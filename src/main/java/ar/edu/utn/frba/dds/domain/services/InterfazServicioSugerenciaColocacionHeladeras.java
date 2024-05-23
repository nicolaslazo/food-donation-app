package ar.edu.utn.frba.dds.domain.services;

import ar.edu.utn.frba.dds.domain.services.entities.UbicacionesSugeridas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfazServicioSugerenciaColocacionHeladeras {
  @GET("sugerencias")
  Call<UbicacionesSugeridas> sugerencias(
      @Query("longitud") float longitud,
      @Query("latitud") float latitud,
      @Query("radioAreaEnMetros") float radioAreaEnMetros
  );
}
