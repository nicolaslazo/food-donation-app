package ar.edu.utn.frba.dds.services.generadorPDF;

import java.util.Map;

public class ReporteFallasHeladera extends PdfGenerator{
  public ReporteFallasHeladera(Map<String, Integer> data)
  {
    super("src/main/reportes/ReporteFallasHeladera.pdf","Cantidad de fallas por heladera", new String[]{"HELADERA", "CANTIDAD"}, data);
  }
}
