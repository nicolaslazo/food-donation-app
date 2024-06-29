package ar.edu.utn.frba.dds.dtos.input.heladera;

import com.google.gson.Gson;
import lombok.NonNull;

import java.time.ZonedDateTime;

public record SolicitudAperturaPorContribucionInputDTO(
    int id,
    @NonNull String fechaRealizadaSerializadaIso8601
) {
  public static SolicitudAperturaPorContribucionInputDTO desdeJson(String rawJson) {
    return new Gson().fromJson(rawJson, SolicitudAperturaPorContribucionInputDTO.class);
  }

  public ZonedDateTime getFechaRealizada() {
    return ZonedDateTime.parse(fechaRealizadaSerializadaIso8601);
  }
}
