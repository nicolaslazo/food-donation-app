package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.email.EnviadorDeMails;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Colaborador {
  @Getter
  @NonNull
  private final Documento documento;
  @Getter
  private final List<Contacto> contactos = new ArrayList<>();
  @Getter
  @NonNull
  private String nombre;
  @Getter
  @NonNull
  private String apellido;
  private LocalDate fechaNacimiento;
  private Ubicacion ubicacion;

  public Colaborador(Documento documento, String nombre, String apellido, ContactoEmail mail) {
    this.documento = documento;
    this.nombre = nombre;
    this.apellido = apellido;
    this.contactos.add(mail);

    String contraseniaTemporaria = GeneradorDeContrasenias.generarContrasenia();
    EnviadorDeMails enviadorDeMails = new EnviadorDeMails();
    enviadorDeMails.enviarMail(mail.getEmail(), contraseniaTemporaria);
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
        "documento=" + documento +
        ", nombre='" + nombre + '\'' +
        ", apellido='" + apellido + '\'' +
        '}';
  }
}