package ar.edu.utn.frba.dds.dtos.input.sensores;

import com.google.gson.Gson;
import lombok.NonNull;

import java.time.ZonedDateTime;

public record SensorTemperaturaInputDTO(int idHeladera, @NonNull String temperatura,
                                        @NonNull String timestampSerializadaIso8601) {
  public static SensorTemperaturaInputDTO desdeJson(@NonNull String json) {
    return new Gson().fromJson(json, SensorTemperaturaInputDTO.class);
  }

  public double getTemperatura() {
    return Double.parseDouble(temperatura);
  }

  public ZonedDateTime getTiempo() {
    return ZonedDateTime.parse(timestampSerializadaIso8601);
  }
}
