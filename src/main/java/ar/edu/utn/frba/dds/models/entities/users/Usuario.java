package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {
  @Column(name = "mail", unique = true, nullable = false)
  @Embedded
  @NonNull Email mail;

  @Column(name = "documento", unique = true, nullable = false, updatable = false)
  @Embedded
  @Getter
  @NonNull Documento documento;

  @Column(name = "primerNombre", nullable = false)
  @Getter
  @NonNull String primerNombre;

  @Column(name = "apellido", nullable = false)
  @Getter
  @NonNull String apellido;

  @Column(name = "fechaNacimiento", updatable = false)
  // Nulificable por el cargador CSV
  LocalDate fechaNacimiento;

  // Idealmente estaríamos usando números de trámite en vez de UUIDs pero el cargador CSV no los soporta
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @Getter
  @NonNull UUID id;

  @ManyToMany
  @JoinTable(
      name = "rolesAsignados",
      joinColumns = @JoinColumn(name = "idRol"),
      inverseJoinColumns = @JoinColumn(name = "idUsuario"))
  @Getter
  @NonNull Set<Rol> roles;

  @Column(name = "contrasenia", nullable = false)
  @NonNull String contrasenia;

  public Usuario(@NonNull Email mail,
                 @NonNull Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 LocalDate fechaNacimiento,
                 @NonNull Set<Rol> roles) {
    this.mail = mail;
    this.documento = documento;
    this.primerNombre = primerNombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.id = UUID.randomUUID();
    this.roles = roles;
    this.contrasenia = GeneradorDeContrasenias.generarContrasenia();

    // TODO: Mover al controller creador de colaboradores
    // new EnviadorDeMails().enviarMail(mail.destinatario(), this.contrasenia);
  }

  protected Usuario() {
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return roles.stream().anyMatch(rol -> rol.tienePermiso(permiso));
  }

  public boolean tienePermiso(@NonNull String nombre) {
    // Asume que la descripción es null
    return tienePermiso(new Permiso(nombre));
  }

  public void assertTienePermiso(@NonNull String nombrePermiso, @NonNull String razon) throws PermisoDenegadoException {
    if (!tienePermiso(nombrePermiso)) throw new PermisoDenegadoException(razon);
  }
}
