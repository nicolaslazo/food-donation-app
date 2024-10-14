package ar.edu.utn.frba.dds.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class FormularioContacto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombrePersona")
  private String name;
  @Column (name = "email")
  private String email;
  @Column (name = "subject")
  private String subject;
  @Column (name = "mensaje")
  private String message;
}
