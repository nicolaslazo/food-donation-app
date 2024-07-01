package ar.edu.utn.frba.dds.dtos.inputs.sensores;

import com.google.gson.Gson;
import lombok.NonNull;

public record SensorMovimientoInputDTO(@NonNull String msg) {
    public static SensorMovimientoInputDTO desdeJson(@NonNull String json) {
        return new Gson().fromJson(json, SensorMovimientoInputDTO.class);
    }

    //TODO ¿Que recibiriamos?
    public String msg() {
        return msg;
    }
}
