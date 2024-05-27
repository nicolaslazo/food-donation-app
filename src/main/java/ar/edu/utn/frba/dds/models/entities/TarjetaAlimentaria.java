package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.NonNull;

public record TarjetaAlimentaria(@NonNull String identificador, @NonNull PersonaVulnerable recipiente,
                                 @NonNull Colaborador proveedor) {
}
