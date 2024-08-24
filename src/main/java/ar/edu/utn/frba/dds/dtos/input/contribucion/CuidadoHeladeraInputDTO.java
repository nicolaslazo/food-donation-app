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
  @NonNull String idColaboradorSerializado;
  double latitud;
  double longitud;

  public CuidadoHeladeraInputDTO(
      @NonNull String nombreHeladera,
      int capacidadEnViandas,
      @NonNull String idColaboradorSerializado,
      double latitud,
      double longitud
  ) {
    this.nombreHeladera = nombreHeladera;
    this.capacidadEnViandas = capacidadEnViandas;
    this.idColaboradorSerializado = idColaboradorSerializado;
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public static CuidadoHeladeraInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, CuidadoHeladeraInputDTO.class);
  }

  public UUID getIdColaborador() {
    return UUID.fromString(idColaboradorSerializado);
  }

  public CoordenadasGeograficas getUbicacion() {
    return new CoordenadasGeograficas(latitud, longitud);
  }
}