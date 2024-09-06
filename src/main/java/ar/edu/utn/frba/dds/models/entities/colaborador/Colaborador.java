package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "colaborador")
@Getter
public class Colaborador {
  @Transient
  static final Rol ROL_DEFAULT =
      new Rol("colaborador", new HashSet<>(List.of(new Permiso("depositarViandas"))));

  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  UUID id;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Usuario.class)
  @MapsId
  @JoinColumn(name = "idUsuario", referencedColumnName = "id", unique = true, nullable = false, updatable = false)
  @NonNull Usuario usuario;

  @Embedded
  @Setter
  CoordenadasGeograficas ubicacion;

  public Colaborador(@NonNull Documento documento,
                     @NonNull String primerNombre,
                     @NonNull String apellido,
                     LocalDate fechaNacimiento,
                     CoordenadasGeograficas ubicacion) {
    this.usuario = new Usuario(
        documento,
        primerNombre,
        apellido,
        fechaNacimiento,
        new HashSet<>(List.of(ROL_DEFAULT)));
    this.ubicacion = ubicacion;
  }

  protected Colaborador() {
  }

  public UUID getId() {
    return usuario.getId();
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