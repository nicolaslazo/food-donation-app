package ar.edu.utn.frba.dds.domain.colaborador;

import ar.edu.utn.frba.dds.domain.contacto.Contacto;
import ar.edu.utn.frba.dds.domain.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.domain.documentacion.Documento;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
  private List<Contacto> contactos = new ArrayList<>();
  @Getter
  private int dineroDonado = 0;
  @Getter
  private int viandasDonadas = 0;
  @Getter
  private int tarjetasAlimentariasEntregadas = 0;
  @Getter
  private int viandasRedistribuidas = 0;

  public Colaborador(Documento documento, String nombre, String apellido, ContactoEmail mail) {
    this.documento = documento;
    this.nombre = nombre;
    this.apellido = apellido;
    this.contactos.add(mail);

    // TODO: Mandar mail de bienvenida cuando el colaborador es creado
  }

  public TipoColaborador getTipoColaborador() {
    if (documento != null) return TipoColaborador.HUMANO;
    return TipoColaborador.JURIDICO;
  }

  public void registrarColaboracionDinero(int cantidad) {
    this.dineroDonado += cantidad;
  }

  public void registrarColaboracionViandas(int cantidad) {
    this.viandasDonadas += cantidad;
  }

  public void registrarColaboracionEntregaTarjetas(int cantidad) {
    this.tarjetasAlimentariasEntregadas += cantidad;
  }

  public void registrarColaboracionRedistribucionViandas(int cantidad) {
    this.viandasRedistribuidas += cantidad;
  }

  public float calcularReconocimiento() {
    // TODO implement here
    return 0.0f;
  }
}