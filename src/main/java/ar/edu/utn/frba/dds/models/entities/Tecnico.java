package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tecnico")
@Getter
@NoArgsConstructor
public final class Tecnico {
  // TODO: el rol capaz debería estar almacenado en un repositorio?
  @Column(name = "id")
  @Id
  @NonNull UUID id;
  @Transient
  private static final Rol ROL_DEFAULT = new Rol("tecnico", new HashSet<>());
  @Column(name = "cuil", unique = true, nullable = false, updatable = false)
  @NonNull String cuil;
  
  @OneToOne(cascade = CascadeType.ALL)
  @MapsId
  @JoinColumn(name = "idUsuario", referencedColumnName = "id")
  @NonNull Usuario usuario; 
  @Embedded
  @NonNull AreaGeografica areaAsignada;

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
        new HashSet<>(List.of(ROL_DEFAULT)));  // TODO: Agregar permisos
    this.cuil = cuil;
    this.areaAsignada = areaAsignada;
  }

  public boolean isDentroDeRango(Heladera heladera) {
    return CalculadoraDistancia.calcular(heladera.getUbicacion(), areaAsignada.centro()) < areaAsignada.radioEnMetros();
  }
}