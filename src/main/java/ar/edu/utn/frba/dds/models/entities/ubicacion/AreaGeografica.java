package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;

public record AreaGeografica(@NonNull Ubicacion centro, float radioEnMetros) {
}