package ar.edu.utn.frba.dds.services.generadorpdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PdfGenerator {

  private String nombreArchivo;
  private String titulo;
  private String[] headersTabla;
  private Map<String, Integer> data;

  @Setter
  private PdfWriter writer;
  @Setter
  private PdfDocument pdf;
  @Setter
  private Document document;

  public PdfGenerator(String nombreArchivo,
                      String tituloTabla,
                      String[] headersTabla,
                      Map<String, Integer> data) {
    this.nombreArchivo = nombreArchivo;
    this.titulo = tituloTabla;
    this.headersTabla = headersTabla;
    this.data = data;
  }

  public boolean generatePdf() {
    try {
      inicializarPDF(); // Inicializar PDF si es necesario
      agregarFecha();
      agregarTitulo();
      agregarTabla();
      document.close();

      System.out.println("PDF generado el archivo correctamente");
      return true;

    } catch (FileNotFoundException e) {
      System.err.println("Error: Archivo no encontrado - " + e.getMessage());
      return false;
    }
  }

  private void inicializarPDF() throws FileNotFoundException {
    if (writer == null || pdf == null || document == null) {
      writer = new PdfWriter(nombreArchivo);
      pdf = new PdfDocument(writer);
      document = new Document(pdf);
    }
  }

  public void agregarTitulo() {
    Paragraph title = new Paragraph(titulo);
    title.setTextAlignment(TextAlignment.CENTER);
    document.add(title);
  }

  public void agregarFecha() {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = currentDate.format(formatter);
    Paragraph date = new Paragraph("Fecha: " + formattedDate);
    date.setTextAlignment(TextAlignment.RIGHT);
    document.add(date);
  }

  public void agregarTabla() {
    Table table = new Table(headersTabla.length);

    // Encabezados de columna
    for (String header : headersTabla) {
      table.addCell(header);
    }

    // Filas de datos
    for (Map.Entry<String, Integer> entry : data.entrySet()) {
      table.addCell(entry.getKey()); // Nombre del producto
      table.addCell(entry.getValue().toString()); // Cantidad vendida
    }

    document.add(table);
  }
}
