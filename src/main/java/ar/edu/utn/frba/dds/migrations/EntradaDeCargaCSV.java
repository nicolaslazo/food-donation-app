package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.contribucion.TipoContribucion;
import ar.edu.utn.frba.dds.domain.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.domain.documentacion.TipoDocumento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record EntradaDeCargaCSV(TipoDocumento tipoDocumento, int numeroDocumento,
                                String nombre, String apellido,
                                ContactoEmail mail, LocalDate fechaDeContribucion,
                                TipoContribucion tipoContribucion, int cantidad) {
  public static EntradaDeCargaCSV crearInstancia(String tipoDoc, String documento,
                                                 String nombre, String apellido,
                                                 String mail, String fechaDeColaboracion,
                                                 String formaDeColaboracion, String cantidad) {
    return new EntradaDeCargaCSV(
        TipoDocumento.fromString(tipoDoc),
        Integer.parseInt(documento),
        nombre,
        apellido,
        new ContactoEmail(mail),
        LocalDate.parse(fechaDeColaboracion, DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        TipoContribucion.fromString(formaDeColaboracion),
        Integer.parseInt(cantidad)
    );
  }
}
