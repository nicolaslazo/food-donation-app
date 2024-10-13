package ar.edu.utn.frba.dds.dtos.input;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@NonNull
@Getter
@Setter
public class FormularioDeContactoDTO {
  private String nombre;
  private String email;
  private String subject;
  private String mensaje;

  public FormularioDeContactoDTO(String name, String email, String subject, String message) {
    this.nombre = name;
    this.email = email;
    this.subject = subject;
    this.mensaje = message;
  }
}
