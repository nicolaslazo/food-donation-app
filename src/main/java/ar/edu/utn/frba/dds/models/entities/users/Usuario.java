package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "usuario")
@ToString
public class Usuario {
  // Idealmente estaríamos usando números de trámite en vez de autoincreases pero el cargador CSV no los soporta
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @NonNull
  Long id;

  @Column(name = "documento", unique = true, nullable = false, updatable = false)
  @Embedded
  @Getter
  @NonNull
  Documento documento;

  @Column(name = "primerNombre", nullable = false)
  @Getter
  @NonNull
  String primerNombre;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
  @Getter
  @Setter
  private List<Contacto> contactos = new ArrayList<>();

  @Column(name = "active", nullable = false)
  @Getter
  @Setter
  @NonNull
  Boolean active = true;

  @Column(name = "apellido", nullable = false)
  @Getter
  @NonNull
  String apellido;

  @Column(name = "fechaNacimiento", updatable = false)
  // Nulificable por el cargador CSV
  LocalDate fechaNacimiento;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
          name = "rolesAsignados",
          joinColumns = @JoinColumn(name = "idUsuario", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "idRol", referencedColumnName = "id"))
  @Getter
  @NonNull
  Set<Rol> roles;

  @Column(name = "contrasenia", nullable = false)
  @NonNull
  String contrasenia;

  public Usuario(@NonNull Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 LocalDate fechaNacimiento,
                 @NonNull Set<Rol> roles) {
    this.documento = documento;
    this.primerNombre = primerNombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.roles = new HashSet<>(roles);
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
