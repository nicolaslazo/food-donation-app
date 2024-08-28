package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "contacto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idUsuario", "destinatario"}),
    @UniqueConstraint(columnNames = {"tipoContacto", "destinatario"})
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoContacto")
public abstract class Contacto {
  @Column(name="id")
  @Id
  @GeneratedValue
  @NonNull Long id;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "idUsuario")
  @NonNull Usuario usuario;

  @Column(name = "destinatario", nullable = false, updatable = false)
  @Getter
  @NonNull String destinatario;

  public abstract void enviarMensaje(String mensaje) throws MensajeAContactoException;
}
