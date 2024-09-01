package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface InterfazServicioSugerenciaColocacionHeladeras {
  @GET("sugerencias")
  Call<List<CoordenadasGeograficas>> sugerencias(
      @Query("latitud") double latitud,
      @Query("longitud") double longitud,
      @Query("radioAreaEnMetros") double radioAreaEnMetros
  );
}
