package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Direccion;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public final class PersonaVulnerable {
  @Getter
  @NonNull Colaborador reclutador;
  @NonNull ZonedDateTime fechaRegistro;
  @NonNull Usuario usuario;
  Direccion domicilio;
  int menoresACargo;

  public PersonaVulnerable(@NonNull Colaborador reclutador,
                           @NonNull ZonedDateTime fechaRegistro,
                           @NonNull Usuario usuario,
                           @NonNull Direccion domicilio,
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