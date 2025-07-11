package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.Getter;

import java.time.ZonedDateTime;

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
  @CsvBindByPosition(position = 4)
  @Getter
  private String mail;
  @CsvCustomBindByPosition(position = 5, converter = FechaDeContribucionConverter.class)
  @CsvDate("dd/MM/yyyy")
  @Getter
  private ZonedDateTime fechaDeContribucion;
  @CsvBindByPosition(position = 6)
  @Getter
  private String tipoDeContribucion;
  @CsvBindByPosition(position = 7)
  @Getter
  private int cantidad;

  public Documento getDocumento() {
    return new Documento(tipoDocumento, valorDocumento);
  }
}