package ar.edu.utn.frba.dds.dtos.inputs.sensores;

import com.google.gson.Gson;
import lombok.NonNull;

public record SensorTemperaturaInputDTO(@NonNull String temperatura) {
    public static SensorTemperaturaInputDTO desdeJson(@NonNull String json) {
        return new Gson().fromJson(json, SensorTemperaturaInputDTO.class);
    }

    public double getTemperatura() {
        return (double) Double.parseDouble(temperatura);
    }

}
