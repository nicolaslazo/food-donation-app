package ar.edu.utn.frba.dds.auth;

import lombok.NonNull;

/* Uso de interfaz para que todos los tipos de filtros interpreten el mismo mensaje de validar y
* poder tratarlos de manera polimorfica al momento de aplicar todos los filtros en la validación final :)*/
public interface FiltrosContrasenia {
   boolean validar(@NonNull String contrasenia);
}
