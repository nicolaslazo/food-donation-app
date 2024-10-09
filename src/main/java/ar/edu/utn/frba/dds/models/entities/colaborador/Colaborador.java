package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.TipoPersonaJuridica;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "colaborador")
@Getter
public class Colaborador {
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NonNull
  Long id;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Usuario.class)
  @MapsId
  @JoinColumn(name = "idUsuario", referencedColumnName = "id", unique = true, nullable = false, updatable = false)
  @NonNull
  Usuario usuario;

  @Embedded
  @Setter
  CoordenadasGeograficas ubicacion;

  public Colaborador(@NonNull Documento documento,
                     @NonNull String primerNombre,
                     @NonNull String apellido,
                     LocalDate fechaNacimiento,
                     CoordenadasGeograficas ubicacion) {
    new SeederRoles().seedRoles();

    this.usuario = new Usuario(
        documento,
        primerNombre,
        apellido,
        fechaNacimiento,
        null,
        // Si tiene documento tiene que ser una persona física
        new HashSet<>(List.of(new RolesRepository().findByName("COLABORADORFISICO").get())));
    this.ubicacion = ubicacion;
  }

  public Colaborador(@NonNull Documento documento,
                     @NonNull String primerNombre,
                     @NonNull String apellido,
                     LocalDate fechaNacimiento,
                     CoordenadasGeograficas ubicacion,
                     String contrasenia,
                     Rol rolColaborador) {
    this.usuario = new Usuario(
        documento,
        primerNombre,
        apellido,
        fechaNacimiento,
        contrasenia,
        new HashSet<>(List.of(rolColaborador)));
    this.ubicacion = ubicacion;
  }

  public Colaborador(
      @NonNull Documento cuit,
      @NonNull TipoPersonaJuridica tipoPersonaJuridica,
      @NonNull String razonSocial,
      LocalDate fechaCreacion,
      @NonNull String contrasenia) {
    this.usuario = new Usuario(
        cuit,
        tipoPersonaJuridica,
        razonSocial,
        fechaCreacion,
        DigestUtils.sha256Hex(contrasenia),
        new HashSet<>(List.of(new RolesRepository().findByName("COLABORADORJURIDICO").get())));
  }

  protected Colaborador() {
  }

  public String getNombreCompleto() {
    return usuario.getPrimerNombre() + " " + usuario.getApellido();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Colaborador that = (Colaborador) o;
    return Objects.equals(getUsuario(), that.getUsuario());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsuario());
  }
}