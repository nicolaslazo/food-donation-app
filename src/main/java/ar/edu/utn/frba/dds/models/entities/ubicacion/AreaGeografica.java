package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;

public record AreaGeografica(@NonNull CoordenadasGeograficas centro, float radioEnMetros) {
}