package ar.edu.utn.frba.dds.models.entities.utils.permisos;

public class SinPermisoSuficienteException extends RuntimeException {
    public SinPermisoSuficienteException() {
    }

    public SinPermisoSuficienteException(String message) {
        super(message);
    }
}
