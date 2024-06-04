package ar.edu.utn.frba.dds.models.entities.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
  private String nombreDeUsuario;
  private String contrasenia;
  private Rol rol;
}
