package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "usuario")
@ToString
public class Usuario {
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @NonNull
  Long id;

  @Column(name = "documento", unique = true)
  @Embedded
  @Getter
  Documento documento;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipoPersonaJuridica")
  TipoPersonaJuridica tipoPersonaJuridica;

  @Column(name = "primerNombre", nullable = false)
  @Getter
  @NonNull
  String primerNombre;

  @Column(name = "activo", nullable = false)
  @Getter
  @Setter
  @NonNull Boolean activo = true;

  @Column(name = "apellido")
  @Getter
  String apellido;

  @Column(name = "fechaNacimiento", updatable = false)
  // Nulificable por el cargador CSV
  LocalDate fechaNacimiento;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "rolesAsignados",
      joinColumns = @JoinColumn(name = "idUsuario", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "idRol", referencedColumnName = "id"))
  @Getter
  @NonNull
  @ToString.Exclude
  Set<Rol> roles;

  @Getter
  @Transient
  String contrasenaPlaintextTemporaria;

  @Column(name = "contrasenia", nullable = false)
  @NonNull String contraseniaHasheada;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
  @Getter
  @Setter
  @ToString.Exclude
  private List<Contacto> contactos = new ArrayList<>();

  public void setRoles(Set<Rol> roles) {
    this.roles = roles;
  }

  // Constructor para personas físicas
  public Usuario(Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 LocalDate fechaNacimiento,
                 String contraseniaHasheada,
                 @NonNull HashSet<Rol> roles) {
    if (contraseniaHasheada == null) {
      contrasenaPlaintextTemporaria = GeneradorDeContrasenias.generarContrasenia();
      contraseniaHasheada = DigestUtils.sha256Hex(contrasenaPlaintextTemporaria);
    }

    this.documento = documento;
    this.primerNombre = primerNombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.roles = roles;
    this.contraseniaHasheada = contraseniaHasheada;
  }

  // Constructor para colaboradores jurídicos
  public Usuario(
      @NonNull Documento cuit,
      @NonNull TipoPersonaJuridica tipoPersonaJuridica,
      @NonNull String nombre,
      LocalDate fechaCreacion,
      @NonNull String contraseniaHasheada,
      @NonNull HashSet<Rol> roles) {
    this.documento = cuit;
    this.tipoPersonaJuridica = tipoPersonaJuridica;
    this.primerNombre = nombre;
    this.fechaNacimiento = fechaCreacion;
    this.contraseniaHasheada = contraseniaHasheada;
    this.roles = roles;
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

  public void agregarRol(@NonNull Rol rol) {
    roles.add(rol);
  }
}
