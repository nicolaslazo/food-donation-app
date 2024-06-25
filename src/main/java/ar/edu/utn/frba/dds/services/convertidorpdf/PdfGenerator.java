package ar.edu.utn.frba.dds.services.convertidorpdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PdfGenerator {

  public String nombreArchivo;
  public String tituloTabla;
  public String[] headersTabla;
  public Map<String, Integer> data;

  public PdfGenerator(String nombreArchivo, String tituloTabla, String[] headersTabla, Map<String, Integer> data) {
    this.nombreArchivo = nombreArchivo;
    this.tituloTabla = tituloTabla;
    this.headersTabla = headersTabla;
    this.data = data;
  }

  private void generatePdf() {
    try {

      // Inicializar el documento PDF
      PdfWriter writer = new PdfWriter(nombreArchivo);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);

      tituloYFecha(document);

      document.add(new Paragraph(tituloTabla));

      Table table = new Table(headersTabla.length);

      // Encabezados de columna
      for (String header : headersTabla) {
        table.addCell(header);
      }

      // Filas de datos
      for (Map.Entry<String, Integer> entry : data.entrySet()) {
        table.addCell(entry.getKey()); // Nombre
        table.addCell(String.valueOf(entry.getValue())); // Cantidad
      }

      document.add(table);

      document.close();

      System.out.println("PDF generado el archivo correctamente");

    } catch (FileNotFoundException e) {
      System.err.println("Error: Archivo no encontrado - " + e.getMessage());
    }
  }

  private static void tituloYFecha(Document document) {
    Paragraph title = new Paragraph("Reporte");
    title.setTextAlignment(TextAlignment.CENTER);
    document.add(title);

    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = currentDate.format(formatter);
    Paragraph date = new Paragraph("Fecha: " + formattedDate);
    date.setTextAlignment(TextAlignment.RIGHT);
    document.add(date);
  }
  
}
