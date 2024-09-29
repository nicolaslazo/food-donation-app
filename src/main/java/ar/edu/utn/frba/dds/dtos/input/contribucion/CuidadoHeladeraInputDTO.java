package ar.edu.utn.frba.dds.dtos.input.contribucion;

import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

public final class CuidadoHeladeraInputDTO {
  @Getter
  @NonNull String nombreHeladera;
  @Getter
  int capacidadEnViandas;
  @Getter
  long idColaborador;
  double latitud;
  double longitud;
  @Getter
  @NonNull String barrio;

  public CuidadoHeladeraInputDTO(
      @NonNull String nombreHeladera,
      int capacidadEnViandas,
      long idColaborador,
      double latitud,
      double longitud,
      @NonNull String barrio
  ) {
    this.nombreHeladera = nombreHeladera;
    this.capacidadEnViandas = capacidadEnViandas;
    this.idColaborador = idColaborador;
    this.latitud = latitud;
    this.longitud = longitud;
    this.barrio = barrio;
  }

  public static CuidadoHeladeraInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, CuidadoHeladeraInputDTO.class);
  }

  public CoordenadasGeograficas getUbicacion() {
    return new CoordenadasGeograficas(latitud, longitud);
  }
}