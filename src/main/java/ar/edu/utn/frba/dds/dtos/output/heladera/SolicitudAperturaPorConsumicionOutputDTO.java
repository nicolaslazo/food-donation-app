package ar.edu.utn.frba.dds.dtos.output.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;
import com.google.gson.Gson;
import lombok.NonNull;

import java.util.UUID;

public record SolicitudAperturaPorConsumicionOutputDTO(
    @NonNull UUID identificadorTarjeta,
    int idSolicitud,
    @NonNull String fechaVencimientoSerializadaIso8601,
    @NonNull Double pesoViandaEnGramos
) {
  public SolicitudAperturaPorConsumicionOutputDTO(SolicitudAperturaPorConsumicion solicitud) {
    this(solicitud.getTarjeta().getId(),
        Math.toIntExact(solicitud.getId()),
        solicitud.getFechaVencimiento().toString(),
        solicitud.getPesoViandaEnGramos());
  }

  public String enJson() {
    return new Gson().toJson(this);
  }
}
