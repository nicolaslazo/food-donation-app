package ar.edu.utn.frba.dds.domain.colaborador;

import ar.edu.utn.frba.dds.domain.contacto.Contacto;
import ar.edu.utn.frba.dds.domain.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.domain.contribucion.Contribucion;
import ar.edu.utn.frba.dds.domain.documentacion.Documento;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.domain.utils.FiltradorContribuciones;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class Colaborador {
  @Getter
  private final Documento documento;
  @NonNull
  @Getter
  private String nombre;
  @NonNull
  @Getter
  private String apellido;
  private LocalDate fechaNacimiento;
  private Ubicacion ubicacion;
  @Getter
  private final List<Contacto> contactos = new ArrayList<>();
  @Getter
  private final List<Contribucion> contribuciones = new ArrayList<Contribucion>();
  private final FiltradorContribuciones filtrador;

  public Colaborador(Documento documento, String nombre, String apellido, ContactoEmail mail,
                     FiltradorContribuciones filtrador) {
    this.documento = documento;
    this.nombre = nombre;
    this.apellido = apellido;
    this.contactos.add(mail);
    this.filtrador = filtrador;

    // TODO: Mandar mail de bienvenida cuando el colaborador es creado
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

  public TipoColaborador getTipoColaborador() {
    if (documento != null) return TipoColaborador.HUMANO;
    return TipoColaborador.JURIDICO;
  }

  // Método para filtrar contribuciones por tipo
  public <T extends Contribucion> List<T> filtrarContribucionesPorTipo(Class<T> tipo) {
    return filtrador.filtrarContribucionesPorTipo(tipo, contribuciones);
  }
}