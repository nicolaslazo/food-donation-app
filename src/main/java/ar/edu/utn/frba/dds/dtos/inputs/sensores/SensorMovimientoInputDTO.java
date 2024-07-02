package ar.edu.utn.frba.dds.dtos.inputs.sensores;

import com.google.gson.Gson;
import lombok.NonNull;

import java.time.ZonedDateTime;

public record SensorMovimientoInputDTO(int idHeladera,@NonNull String timestampIso8601) {
    public static SensorMovimientoInputDTO desdeJson(@NonNull String json) {
        return new Gson().fromJson(json, SensorMovimientoInputDTO.class);
    }

    public ZonedDateTime getTiempo() {
        return ZonedDateTime.parse(timestampIso8601);
    }
}
