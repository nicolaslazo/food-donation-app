package ar.edu.utn.frba.dds.models.entities.documentacion;

import lombok.NonNull;

public record Documento(@NonNull TipoDocumento tipo, int valor) {
}