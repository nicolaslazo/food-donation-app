package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "personaVulnerable")
@Getter
public class PersonaVulnerable {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @Getter
  Long id;

  @OneToOne(fetch = FetchType.LAZY, targetEntity = Usuario.class)
  @MapsId
  @JoinColumn(name = "idUsuario", referencedColumnName = "id", nullable = false, updatable = false)
  @NonNull Usuario usuario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idReclutador", referencedColumnName = "idUsuario", nullable = false, updatable = false)
  @Getter
  @NonNull Colaborador reclutador;

  @Column(name = "fechaRegistro", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaRegistro;

  @OneToOne(fetch = FetchType.LAZY, targetEntity = DireccionResidencia.class)
  @JoinColumn(name = "domicilio", unique = true)
  DireccionResidencia domicilio;

  @Column(name = "menoresACargo", nullable = false)
  @NonNull Integer menoresACargo;

  public long getCantidadViandasPermitidasPorDia() {
    return 4 + menoresACargo * 2;
  }

  public PersonaVulnerable(@NonNull Usuario usuario,
                           @NonNull Colaborador reclutador,
                           @NonNull ZonedDateTime fechaRegistro,
                           DireccionResidencia domicilio,
                           int menoresACargo) {
    this.usuario = usuario;
    this.reclutador = reclutador;
    this.fechaRegistro = fechaRegistro;
    this.domicilio = domicilio;
    this.menoresACargo = menoresACargo;
  }

  protected PersonaVulnerable() {
  }
}