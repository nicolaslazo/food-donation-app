package ar.edu.utn.frba.dds.services.convertidorpdf;

import java.util.Map;

public class ReporteViandasColaborador extends PdfGenerator{
  public ReporteViandasColaborador(Map<String, Integer> data)
  {
    super("src/main/reportes/ReporteViandasPorColaborador.pdf","Cantidad de viandas donadas por colaborador", new String[]{"COLABORADOR", "CANTIDAD"}, data);
  }
}
