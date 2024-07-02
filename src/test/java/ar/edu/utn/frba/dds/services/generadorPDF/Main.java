package ar.edu.utn.frba.dds.services.generadorPDF;

import java.util.HashMap;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    String nombreArchivo = "reporte.pdf";
    String tituloTabla = "Reporte de Ventas";
    String[] headersTabla = {"Producto", "Cantidad Vendida"};
    Map<String, Integer> data = new HashMap<>();
    data.put("Producto A", 15);
    data.put("Producto B", 20);
    data.put("Producto C", 10);

    PdfGenerator pdfGenerator = new PdfGenerator(nombreArchivo, tituloTabla, headersTabla, data);

    boolean result = pdfGenerator.generatePdf();

    if (result) {
      System.out.println("PDF generado correctamente.");
    } else {
      System.out.println("Error al generar el PDF.");
    }
  }
}
