package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Colaborador {
  @Getter
  private final @NonNull Documento documento;
  @Getter
  private final @NonNull List<Contacto> contactos;
  private final LocalDate fechaNacimiento;
  private final @NonNull Usuario usuario;
  @Getter
  private @NonNull String nombre;
  @Getter
  private @NonNull String apellido;
  @Getter
  private Ubicacion ubicacion;

  public Colaborador(@NonNull Documento documento,
                     @NonNull Email mail,
                     LocalDate fechaNacimiento,
                     @NonNull String nombre,
                     @NonNull String apellido,
                     Ubicacion ubicacion) {
    this.documento = documento;
    this.contactos = new ArrayList<>(List.of(mail));
    this.usuario = new Usuario(mail, new Rol());
    this.contribuciones = new ArrayList<>();
    this.fechaNacimiento = fechaNacimiento;
    this.nombre = nombre;
    this.apellido = apellido;
    this.ubicacion = ubicacion;
  }

  public <T extends Contribucion> List<T> getContribuciones(@NonNull Class<T> tipo) {
    return contribuciones.stream().filter(tipo::isInstance).map(tipo::cast).collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Colaborador that = (Colaborador) o;
    return Objects.equals(getDocumento(), that.getDocumento());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDocumento());
  }

  @Override
  public String toString() {
    return "Colaborador{" +
        "documento=" +
        documento + ", nombre='" +
        nombre + '\'' + ", apellido='" +
        apellido + '\'' +
        '}';
  }
}