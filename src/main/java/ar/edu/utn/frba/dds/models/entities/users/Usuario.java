package ar.edu.utn.frba.dds.models.entities.users;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class Usuario {
  @NonNull Email mail;
  @Getter
  @NonNull Documento documento;
  @Getter
  @NonNull String primerNombre;
  @Getter
  @NonNull String apellido;
  // Nulificable por el cargador CSV
  LocalDate fechaNacimiento;
  // Idealmente estaríamos usando números de trámite en vez de UUIDs pero el cargador CSV no los soporta
  @Getter
  @NonNull UUID id;
  @Getter
  @NonNull Set<Rol> roles;
  @NonNull String contrasenia;

  public Usuario(@NonNull Email mail,
                 @NonNull Documento documento,
                 @NonNull String primerNombre,
                 @NonNull String apellido,
                 LocalDate fechaNacimiento,
                 @NonNull Set<Rol> roles) {
    this.mail = mail;
    this.documento = documento;
    this.primerNombre = primerNombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.id = UUID.randomUUID();
    this.roles = roles;
    this.contrasenia = GeneradorDeContrasenias.generarContrasenia();

    // TODO: Mover al controller creador de colaboradores
    // new EnviadorDeMails().enviarMail(mail.destinatario(), this.contrasenia);
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return roles.stream().anyMatch(rol -> rol.tienePermiso(permiso));
  }

  public boolean tienePermiso(@NonNull String nombre) {
    // Asume que la descripción es null
    return tienePermiso(new Permiso(nombre, null));
  }

  public void assertTienePermiso(@NonNull String nombrePermiso, @NonNull String razon) throws PermisoDenegadoException {
    if (!tienePermiso(nombrePermiso)) throw new PermisoDenegadoException(razon);
  }
}
