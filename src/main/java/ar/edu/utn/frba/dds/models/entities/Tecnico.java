package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
@Table(name = "tecnico")
@Getter
@ToString
public class Tecnico {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "idUsuario", referencedColumnName = "id")
  @MapsId
  @NonNull
  Usuario usuario;

  @Column(name = "cuil", unique = true, nullable = false, updatable = false)
  @NonNull
  String cuil;

  @Embedded
  @NonNull
  AreaGeografica areaAsignada;

  public Tecnico(@NonNull Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 @NonNull LocalDate fechaNacimiento,
                 @NonNull String cuil,
                 @NonNull AreaGeografica areaAsignada,
                 String contrasenia,
                 @NonNull Rol rolTecnico) {
    new SeederRoles().seedRoles();

    this.usuario = new Usuario(
        documento,
        primerNombre,
        apellido,
        fechaNacimiento,
        contrasenia,
        new HashSet<>(List.of(rolTecnico))
    );  // TODO: Agregar permisos
    this.cuil = cuil;
    this.areaAsignada = areaAsignada;
  }

  protected Tecnico() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tecnico tecnico)) return false;
    return Objects.equals(getCuil(), tecnico.getCuil());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCuil());
  }

  public boolean isDentroDeRango(Heladera heladera) {
    return CalculadoraDistancia.calcular(heladera.getUbicacion(), areaAsignada.getCentro()) < areaAsignada.getRadioEnMetros();
  }
}