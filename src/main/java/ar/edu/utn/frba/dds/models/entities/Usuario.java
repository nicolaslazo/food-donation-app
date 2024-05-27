package ar.edu.utn.frba.dds.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private String nombreDeUsuario;
    private String contrasenia;
    private Rol rol;
}
