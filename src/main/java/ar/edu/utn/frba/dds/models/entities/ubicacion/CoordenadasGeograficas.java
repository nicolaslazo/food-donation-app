package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;

public record CoordenadasGeograficas(@NonNull Double latitud, @NonNull Double longitud) {
}
