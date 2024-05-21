package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.domain.PersonaVulnerable;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.NonNull;

public record TarjetaAlimentaria(@NonNull String identificador, @NonNull PersonaVulnerable recipiente,
                                 @NonNull Colaborador proveedor) {


}
