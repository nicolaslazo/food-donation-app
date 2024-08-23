package ar.edu.utn.frba.dds.models.entities.ubicacion;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.NonNull;

public record DireccionResidencia(
    @NonNull Usuario usuario,
    String unidad,
    String piso,
    @NonNull String numeroDeCasa,
    @NonNull String calle,
    @NonNull String codigoPostal,
    @NonNull String ciudad,
    @NonNull String provincia,
    @NonNull String pais) {
}
