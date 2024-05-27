package ar.edu.utn.frba.dds.models.entities;

import java.util.ArrayList;
import java.util.Collection;

public class Rol {
    private String nombre;
    private Collection<Permiso> permisos;

    public boolean tenesPermiso(Permiso permiso) {
        return permisos
                .stream().anyMatch(p -> p.equals(permiso));
    }
}
