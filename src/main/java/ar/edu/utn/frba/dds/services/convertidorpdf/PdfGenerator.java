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

public class PdfGenerator {

  public static void main(String[] args) {
    String directoryPath = "src/main/reportes";
    crearSiNoExiste(directoryPath);

    generatePdf(directoryPath + "/ReporteFallasHeladeras.pdf", "Cantidad de fallas por heladera", new String[]{"ID HELADERA", "CANTIDAD"},
        new String[][]{
            {"5675", "25"},
            {"324", "30"}
        });

    generatePdf(directoryPath + "/ReporteMovimientoHeladeras.pdf", "Cantidad de movimientos por heladera", new String[]{"ID HELADERA", "CANTIDAD"},
        new String[][]{
            {"234", "2"},
            {"2345", "15"}
        });

    generatePdf(directoryPath + "/ReporteCantidadViandasColaborador.pdf", "Cantidad de viandas donadas por colaborador", new String[]{"Nombre", "Cantidad"},
        new String[][]{
            {"Messi", "32"},
            {"Merentiel", "45"}
        });
  }

  private static void generatePdf(String fileName, String tableTitle, String[] headers, String[][] data) {
    try {
      // Inicializar el documento PDF
      PdfWriter writer = new PdfWriter(fileName);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);

      tituloYFecha(document, "Reporte");

      document.add(new Paragraph(tableTitle));

      Table table = new Table(headers.length);

      // Encabezados de columna
      for (String header : headers) {
        table.addCell(header);
      }

      // Filas de datos
      for (String[] row : data) {
        for (String cell : row) {
          table.addCell(cell);
        }
      }

      document.add(table);

      document.close();

      System.out.println("PDF generado el archivo correctamente");

    } catch (FileNotFoundException e) {
      System.err.println("Error: Archivo no encontrado - " + e.getMessage());
    }
  }

  private static void tituloYFecha(Document document, String titleText) {
    Paragraph title = new Paragraph(titleText);
    title.setTextAlignment(TextAlignment.CENTER);
    document.add(title);

    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = currentDate.format(formatter);
    Paragraph date = new Paragraph("Fecha: " + formattedDate);
    date.setTextAlignment(TextAlignment.RIGHT);
    document.add(date);
  }

  private static void crearSiNoExiste(String directoryPath) {
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }
}
