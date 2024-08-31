package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;
import javax.persistence.Embeddable;

@Embeddable
public record AreaGeografica(@NonNull CoordenadasGeograficas centro, float radioEnMetros) {
}