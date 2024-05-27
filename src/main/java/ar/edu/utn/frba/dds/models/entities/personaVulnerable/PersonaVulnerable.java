package ar.edu.utn.frba.dds.models.entities.personaVulnerable;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

public class PersonaVulnerable {
  @NonNull
  private final Documento documento;
  @NonNull
  private final Colaborador reclutador;
  @NonNull
  private final String nombre;
  @NonNull
  private final String apellido;
  private final ZonedDateTime fechaNacimiento;
  @NonNull
  private final ZonedDateTime fechaRegistrada;
  @Setter
  private Ubicacion ubicacion;
  @Getter
  @Setter
  private int id;
  @NonNull
  @Setter
  private int menoresACargo;

  public PersonaVulnerable(@NonNull Documento documento, @NonNull Colaborador reclutador, @NonNull String nombre,
                           @NonNull String apellido, ZonedDateTime fechaNacimiento,
                           @NonNull ZonedDateTime fechaRegistrada, Ubicacion ubicacion,
                           @NonNull int menoresACargo) {
    this.documento = documento;
    this.reclutador = reclutador;
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.fechaRegistrada = fechaRegistrada;
    this.ubicacion = ubicacion;
    this.menoresACargo = menoresACargo;
  }

  public void solicitarVianda(Vianda vianda, Heladera heladera) {
    // TODO implement here
  }
}
