package ar.edu.utn.frba.dds.dtos.input.tecnico;

import ar.edu.utn.frba.dds.dtos.input.colaborador.ColaboradorInputDTO;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.services.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
  String contrasenia;

  public static TecnicoInputDTO desdeJson(@NonNull String json) {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    return gson.fromJson(json, TecnicoInputDTO.class);
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
