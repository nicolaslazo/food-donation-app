package ar.edu.utn.frba.dds.dtos.input.contribucion;

import ar.edu.utn.frba.dds.models.entities.heladera.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NonNull;


@Getter
public final class CuidadoHeladeraInputDTO {
  @NonNull String nombreHeladera;
  @NonNull ModeloHeladera modeloHeladera;
  @NonNull Long idColaborador;
  @NonNull Double latitud;
  @NonNull Double longitud;
  @NonNull String barrio;

  public CuidadoHeladeraInputDTO(
      @NonNull String nombreHeladera,
      ModeloHeladera modeloHeladera,
      Long idColaborador,
      Double latitud,
      Double longitud,
      @NonNull String barrio
  ) {
    this.nombreHeladera = nombreHeladera;
    this.modeloHeladera = modeloHeladera;
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