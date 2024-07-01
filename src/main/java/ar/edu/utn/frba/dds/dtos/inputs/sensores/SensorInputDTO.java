package ar.edu.utn.frba.dds.dtos.inputs.sensores;

import com.google.gson.Gson;
import lombok.NonNull;

//TODO tengo que hacer un diferenciacion entre sensorInputTemperatura y Movimiento?
public record SensorInputDTO(@NonNull String temperatura) {
    public static SensorInputDTO desdeJson(@NonNull String json) {
        return new Gson().fromJson(json, SensorInputDTO.class);
    }

    // Seria algo asi? o estoy perdido
    public double getTemperatura() {
        return (double) Double.parseDouble(temperatura);
    }

}
