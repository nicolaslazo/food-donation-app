package ar.edu.utn.frba.dds.dtos.output.heladera;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportesDTO {
  private String titulo;
  private String rutaArchivo;

  public ReportesDTO(String titulo, String rutaArchivo) {
    this.titulo = titulo;
    this.rutaArchivo = rutaArchivo;
  }
}
