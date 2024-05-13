package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.contribucion.TipoContribucion;
import ar.edu.utn.frba.dds.domain.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.domain.documentacion.Documento;
import ar.edu.utn.frba.dds.domain.documentacion.TipoDocumento;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Representa una línea de una migración de colaboraciones legacy en formato CSV.
 * Su único trabajo es implementar la API de OpenCSV para mapear las strings de cada línea
 * a los objetos equivalentes de nuestro dominio.
 * Por ejemplo, esta clase garantiza que si hay una fecha esta va a ser parseada de acuerdo al formato predefinido.
 */
public class EntradaDeCargaCSV {
  @CsvCustomBindByPosition(position = 0, converter = TipoDocumentoConverter.class)
  private TipoDocumento tipoDocumento;
  @CsvBindByPosition(position = 1)
  private int valorDocumento;
  @CsvBindByPosition(position = 2)
  @Getter
  private String nombre;
  @CsvBindByPosition(position = 3)
  @Getter
  private String apellido;
  @CsvCustomBindByPosition(position = 4, converter = ContactoEmailConverter.class)
  @Getter
  private ContactoEmail mail;
  @CsvBindByPosition(position = 5)
  @CsvDate("dd/MM/yyyy")
  @Getter
  private LocalDate fechaDeContribucion;
  @CsvCustomBindByPosition(position = 6, converter = TipoContribucionConverter.class)
  @Getter
  private TipoContribucion tipoDeContribucion;
  @CsvBindByPosition(position = 7)
  @Getter
  private int cantidad;

  public Documento getDocumento() {
    return new Documento(tipoDocumento, valorDocumento);
  }
}