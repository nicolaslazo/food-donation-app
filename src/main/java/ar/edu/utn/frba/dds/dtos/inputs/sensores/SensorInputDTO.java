package ar.edu.utn.frba.dds.dtos.inputs.sensores;

import com.google.gson.Gson;
import lombok.NonNull;

//TODO no sabria como seguir
public record SensorInputDTO() {
    public static SensorInputDTO desdeJson(@NonNull String json) {
        return new Gson().fromJson(json, SensorInputDTO.class);
    }
}
