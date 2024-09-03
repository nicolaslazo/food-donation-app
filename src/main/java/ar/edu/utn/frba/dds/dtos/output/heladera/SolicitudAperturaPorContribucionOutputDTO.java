package ar.edu.utn.frba.dds.dtos.output.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import com.google.gson.Gson;
import lombok.NonNull;

import java.util.UUID;

public record SolicitudAperturaPorContribucionOutputDTO(
    @NonNull UUID identificadorTarjeta,
    int idSolicitud,
    @NonNull String fechaVencimientoSerializadaIso8601,
    @NonNull double[] pesosViandasEnGramos
) {
  public SolicitudAperturaPorContribucionOutputDTO(SolicitudAperturaPorContribucion solicitud) {
    this(solicitud.getTarjeta().getId(),
        Math.toIntExact(solicitud.getId()),
        solicitud.getFechaVencimiento().toString(),
        solicitud.getPesosDeViandasEnGramos());
  }

  public String enJson() {
    return new Gson().toJson(this);
  }
}
