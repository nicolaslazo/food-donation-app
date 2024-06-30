package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public final class PersonaVulnerable {
  @Getter
  final @NonNull Documento documento;
  final @NonNull Colaborador reclutador;
  final @NonNull LocalDate fechaDeNacimiento;
  final @NonNull ZonedDateTime fechaRegistro;
  final @NonNull Usuario usuario;
  @NonNull String nombre;
  @NonNull String apellido;
  Ubicacion domicilio;
  int menoresACargo;

  public PersonaVulnerable(@NonNull Documento documento, @NonNull Colaborador reclutador, @NonNull String nombre,
                           @NonNull String apellido, @NonNull LocalDate fechaDeNacimiento,
                           ZonedDateTime fechaRegistro, Ubicacion domicilio, int menoresACargo,
                           @NonNull Usuario usuario) {
    this.documento = documento;
    this.reclutador = reclutador;
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaDeNacimiento = fechaDeNacimiento;
    this.fechaRegistro = fechaRegistro != null ? fechaRegistro : ZonedDateTime.now();
    this.domicilio = domicilio;
    this.menoresACargo = menoresACargo;
    this.usuario = usuario;
  }
}