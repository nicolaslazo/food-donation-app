package ar.edu.utn.frba.dds.dtos.input.tecnico;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class TecnicoInputDTO {
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
  String cuil;
  @NonNull
  Double latitud;
  @NonNull
  Double longitud;
  @NonNull
  Float radioEnMetros;

  public static TecnicoInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, TecnicoInputDTO.class);
  }

  public AreaGeografica getAreaGeografica() {
    return new AreaGeografica(
            new CoordenadasGeograficas(latitud, longitud),
            radioEnMetros
    );
  }

  public Documento getDocumento() {
    assert TipoDocumento.fromString(tipoDocumento) != null;
    return new Documento(
            TipoDocumento.fromString(tipoDocumento),
            numeroDocumento
    );
  }
}
