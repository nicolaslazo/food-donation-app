package ar.edu.utn.frba.dds.services.reconocimientosextra;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfazServicioReconocimientosExtra {
  @GET("colabs/recomendacion")
  Call<ColeccionReporteReconocimientosColaborador> solicitarRecomendaciones(
      @Query("puntos") int puntos,
      @Query("donaciones") int minimoDonaciones,
      @Query("max") Integer maximo
  );
}
