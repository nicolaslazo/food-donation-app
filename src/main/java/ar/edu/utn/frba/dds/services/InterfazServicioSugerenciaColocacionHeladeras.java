package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.entities.ubicacion.Coordenadas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface InterfazServicioSugerenciaColocacionHeladeras {
  @GET("sugerencias")
  Call<List<Coordenadas>> sugerencias(
      @Query("longitud") double longitud,
      @Query("latitud") double latitud,
      @Query("radioAreaEnMetros") double radioAreaEnMetros
  );
}
