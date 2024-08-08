package ar.edu.utn.frba.dds.dtos.input.heladera;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.Objects;

public final class SolicitudAperturaPorContribucionInputDTO {
  @Getter
  @NonNull Integer id;  // Tiene que ser un @NonNull Integer, no int, para mantener compatibilidad con JSON
  @Getter
  @NonNull Boolean esExtraccion;
  @NonNull String fechaRealizadaSerializadaIso8601;

  public SolicitudAperturaPorContribucionInputDTO(
      int id,
      boolean esExtraccion,
      @NonNull String fechaRealizadaSerializadaIso8601
  ) {
    this.id = id;
    this.esExtraccion = esExtraccion;
    this.fechaRealizadaSerializadaIso8601 = fechaRealizadaSerializadaIso8601;
  }

  public static SolicitudAperturaPorContribucionInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, SolicitudAperturaPorContribucionInputDTO.class);
  }

  public ZonedDateTime getFechaRealizada() {
    return ZonedDateTime.parse(fechaRealizadaSerializadaIso8601);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (SolicitudAperturaPorContribucionInputDTO) obj;
    return Objects.equals(this.id, that.id) &&
        Objects.equals(this.fechaRealizadaSerializadaIso8601, that.fechaRealizadaSerializadaIso8601);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fechaRealizadaSerializadaIso8601);
  }
}
