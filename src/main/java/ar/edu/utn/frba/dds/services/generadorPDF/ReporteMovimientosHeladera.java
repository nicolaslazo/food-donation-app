package ar.edu.utn.frba.dds.services.generadorPDF;

import java.util.Map;

public class ReporteMovimientosHeladera extends PdfGenerator{
  public ReporteMovimientosHeladera(Map<String, Integer> data)
  {
    super("src/main/reportes/ReporteMovimientosHeladera.pdf","Cantidad de heladeras retiradas y colocadas por heladera", new String[]{"HELADERA", "CANTIDAD"}, data);
  }
}
