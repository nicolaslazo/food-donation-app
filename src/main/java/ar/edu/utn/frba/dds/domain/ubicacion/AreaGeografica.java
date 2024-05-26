package ar.edu.utn.frba.dds.domain.ubicacion;

import lombok.NonNull;

public record AreaGeografica(@NonNull Ubicacion centro, float radioEnMetros) {
}