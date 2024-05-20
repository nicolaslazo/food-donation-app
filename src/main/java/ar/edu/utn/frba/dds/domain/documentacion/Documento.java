package ar.edu.utn.frba.dds.domain.documentacion;

import lombok.NonNull;

public record Documento(@NonNull TipoDocumento tipo, @NonNull int valor) {
}