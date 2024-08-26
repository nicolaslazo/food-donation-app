package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;

import javax.persistence.Embeddable;

@Embeddable
public record CoordenadasGeograficas(@NonNull Double latitud, @NonNull Double longitud) {
}
