package ar.edu.utn.frba.dds.dtos.input.colaborador;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class ColaboradorInputDTO {
  @NonNull
  String tipoDocumento;
  @NonNull
  Integer numeroDocumento;
  @NonNull
  String primerNombre;
  @NonNull
  String apellido;
  @NonNull
  LocalDate fechaNacimiento;
  @NonNull
  Double latitud;
  @NonNull
  Double longitud;

  public static ColaboradorInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, ColaboradorInputDTO.class);
  }

  public CoordenadasGeograficas getCoordenadasGeograficas() {
    return new CoordenadasGeograficas(latitud, longitud);
  }

  public Documento getDocumento() {
    assert TipoDocumento.fromString(tipoDocumento) != null;
    return new Documento(
            TipoDocumento.fromString(tipoDocumento),
            numeroDocumento
    );
  }

}
