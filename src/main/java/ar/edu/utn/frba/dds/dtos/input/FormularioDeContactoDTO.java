package ar.edu.utn.frba.dds.dtos.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormularioDeContactoDTO {
  private String name;
  private String email;
  private String subject;
  private String message;

  public FormularioDeContactoDTO(String name, String email, String subject, String message) {
    this.name = name;
    this.email = email;
    this.subject = subject;
    this.message = message;
  }
}
