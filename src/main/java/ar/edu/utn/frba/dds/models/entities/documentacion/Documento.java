package ar.edu.utn.frba.dds.models.entities.documentacion;

import lombok.NonNull;

import javax.persistence.Embeddable;

@Embeddable
public record Documento(@NonNull TipoDocumento tipo, @NonNull Integer valor) {
}