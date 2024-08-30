package ar.edu.utn.frba.dds.dtos.input.heladera.incidente;

import com.google.gson.Gson;
import lombok.NonNull;

import java.time.ZonedDateTime;

public record IncidenteInputDTO(Long idHeladera, @NonNull String tipoIncidente,
                                @NonNull String fechaRealizadaSerializadaIso8601) {
  public static IncidenteInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, IncidenteInputDTO.class);
  }

  public ZonedDateTime getFecha() {
    return ZonedDateTime.parse(fechaRealizadaSerializadaIso8601);
  }
}
