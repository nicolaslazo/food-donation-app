package ar.edu.utn.frba.dds.models.entities.contacto;

import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "mensajesSuscripciones")
@Getter
public class Mensaje {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  Long id;

  @ManyToOne(targetEntity = Contacto.class)
  @JoinColumn(name = "idContacto", referencedColumnName = "id", nullable = false)
  @NonNull
  Contacto contacto;

  @Column(name = "contenido", nullable = false)
  @NonNull
  String contenido;

  @Column(name = "fechaEnvio", nullable = false)
  @NonNull ZonedDateTime fechaEnvio;

  public Mensaje(@NonNull Contacto contacto, @NonNull String contenido, @NonNull ZonedDateTime fechaEnvio) {
    this.contacto = contacto;
    this.contenido = contenido;
    this.fechaEnvio = fechaEnvio;
  }

}
