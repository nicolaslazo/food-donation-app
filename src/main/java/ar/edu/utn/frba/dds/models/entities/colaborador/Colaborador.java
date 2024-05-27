package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.auth.GeneradorDeContrasenias;
import ar.edu.utn.frba.dds.email.EnviadorDeMails;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.models.entities.contribucion.Contribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Colaborador {
  @NonNull
  @Getter
  private final Documento documento;
  @Getter
  private final List<Contacto> contactos = new ArrayList<>();
  @Getter
  private final List<Contribucion> contribuciones = new ArrayList<>();
  @NonNull
  @Getter
  private String nombre;
  @NonNull
  @Getter
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

  public <T extends Contribucion> List<T> getContribuciones(Class<T> tipo) {
    return contribuciones.stream()
        .filter(tipo::isInstance)
        .map(tipo::cast)
        .collect(Collectors.toList());
  }

  public float getDineroDonado() {
    return getContribuciones(Dinero.class).stream().map(Dinero::getMonto).reduce(0f, Float::sum);
  }

  public int getNumeroViandasDistribuidas() {
    return getContribuciones(RedistribucionViandas.class).stream().mapToInt(RedistribucionViandas::getNumeroViandas).sum();
  }

  public int getNumeroViandasDonadas() {
    return getContribuciones(DonacionViandas.class).stream().mapToInt(DonacionViandas::getNumeroViandas).sum();
  }

  public int getNumeroTarjetasRepartidas() {
    return getContribuciones(EntregaTarjetas.class).stream().mapToInt(EntregaTarjetas::getNumeroTarjetas).sum();
  }

  public int getMesesCumulativosCuidadoHeladeras() {
    return getContribuciones(CuidadoHeladera.class)
        .stream()
        .map(CuidadoHeladera::getHeladera)
        .mapToInt(Heladera::mesesActiva)
        .sum();
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