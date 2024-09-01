package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.time.ZonedDateTime;
import java.util.UUID;

public final class PersonaVulnerable {
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

  public PersonaVulnerable(@NonNull Colaborador reclutador,
                           @NonNull ZonedDateTime fechaRegistro,
                           @NonNull Usuario usuario,
                           @NonNull DireccionResidencia domicilio,
                           int menoresACargo) {
    this.reclutador = reclutador;
    this.fechaRegistro = fechaRegistro;
    this.usuario = usuario;
    this.domicilio = domicilio;
    this.menoresACargo = menoresACargo;
  }

  public UUID getId() {
    return usuario.getId();
  }
}