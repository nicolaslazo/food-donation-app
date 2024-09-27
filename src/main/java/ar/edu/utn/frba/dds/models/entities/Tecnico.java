package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;

import lombok.NonNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tecnico")
@Getter
@EqualsAndHashCode
@ToString
public class Tecnico {
  // TODO: el rol capaz debería estar almacenado en un repositorio?

  //TODO: Revisar el mapeo, no se si era lo pedido
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "idUsuario", referencedColumnName = "id")
  @MapsId
  @Id
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
                 @NonNull AreaGeografica areaAsignada) {
    this.usuario = new Usuario(
            documento,
            primerNombre,
            apellido,
            fechaNacimiento,
            new HashSet<>(List.of())
    );  // TODO: Agregar permisos
    this.cuil = cuil;
    this.areaAsignada = areaAsignada;
  }

  public Tecnico(@NonNull Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 @NonNull LocalDate fechaNacimiento,
                 @NonNull String cuil,
                 @NonNull AreaGeografica areaAsignada,
                 @NonNull Rol rolTecnico) {
    this.usuario = new Usuario(
            documento,
            primerNombre,
            apellido,
            fechaNacimiento,
            new HashSet<>(List.of(rolTecnico))
    );  // TODO: Agregar permisos
    this.cuil = cuil;
    this.areaAsignada = areaAsignada;
  }

  protected Tecnico() {
  }

  public boolean isDentroDeRango(Heladera heladera) {
    return CalculadoraDistancia.calcular(heladera.getUbicacion(), areaAsignada.getCentro()) < areaAsignada.getRadioEnMetros();
  }
}