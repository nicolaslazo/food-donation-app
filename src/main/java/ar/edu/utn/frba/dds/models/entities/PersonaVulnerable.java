package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "personaVulnerable")
public class PersonaVulnerable {
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  UUID id;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "idUsuario", referencedColumnName = "id", nullable = false, updatable = false)
  @NonNull Usuario usuario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idReclutador", referencedColumnName = "id", nullable = false, updatable = false)
  @Getter
  @NonNull Colaborador reclutador;

  @Column(name = "fechaRegistro", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaRegistro;

  @Column(name = "domicilio")
  @Embedded
  DireccionResidencia domicilio;

  @Column(name = "menoresACargo", nullable = false)
  int menoresACargo;

  public PersonaVulnerable(@NonNull Usuario usuario,
                           @NonNull Colaborador reclutador,
                           @NonNull ZonedDateTime fechaRegistro,
                           @NonNull DireccionResidencia domicilio,
                           int menoresACargo) {
    this.usuario = usuario;
    this.reclutador = reclutador;
    this.fechaRegistro = fechaRegistro;
    this.domicilio = domicilio;
    this.menoresACargo = menoresACargo;
  }

  protected PersonaVulnerable() {
  }

  public UUID getId() {
    return usuario.getId();
  }
}