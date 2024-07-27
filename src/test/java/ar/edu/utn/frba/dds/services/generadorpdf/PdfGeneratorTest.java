package ar.edu.utn.frba.dds.services.generadorpdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

public class PdfGeneratorTest {
   @Setter
   private PdfWriter writer;
    @Setter private PdfDocument pdf;
    @Setter private Document document;

    @BeforeEach
    public void setUp() {
      writer = Mockito.mock(PdfWriter.class);
      pdf = Mockito.mock(PdfDocument.class);
      document = Mockito.mock(Document.class);
    }

    @Test
    public void testInicializarPDF()  {
      PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
          new String[]{"Heladera", "Cantidad"}, new HashMap<>());

      pdfGenerator.setWriter(writer);
      pdfGenerator.setPdf(pdf);
      pdfGenerator.setDocument(document);

      pdfGenerator.generatePdf();

      Mockito.verify(document).close();
      assertTrue(pdfGenerator.generatePdf(), "generatePdf() retorna true");
    }

    @Test
    public void testAgregarTitulo() {
      Document mockDocument = Mockito.mock(Document.class);

      PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
          new String[]{"Heladera", "Cantidad"}, new HashMap<>());

      pdfGenerator.setDocument(mockDocument);

      pdfGenerator.agregarTitulo();

      Mockito.verify(mockDocument, times(1)).add(Mockito.any(Paragraph.class));
    }

    @Test
    public void testAgregarFecha() {
      Document mockDocument = Mockito.mock(Document.class);

      PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
          new String[]{"Heladera", "Cantidad"}, new HashMap<>());

      pdfGenerator.setDocument(mockDocument);

      pdfGenerator.agregarFecha();

      Mockito.verify(mockDocument, times(1)).add(Mockito.any(Paragraph.class));
    }

    @Test
    public void testAgregarTabla() {
      Document mockDocument = Mockito.mock(Document.class);
      PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
          new String[]{"Heladera", "Cantidad"}, new HashMap<>());

      pdfGenerator.setDocument(mockDocument);

      pdfGenerator.agregarTabla();

      Mockito.verify(mockDocument).add(Mockito.any(Table.class));
    }

    @Test
    public void testGeneratePdf() {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PdfWriter pdfWriter = new PdfWriter(outputStream);

      PdfDocument pdfDocument = new PdfDocument(pdfWriter);
      Document doc = new Document(pdfDocument);

      Map<String, Integer> data = new HashMap<>();
      data.put("Heladera A", 10);
      data.put("Heladera B", 20);

      PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
          new String[]{"Heladera", "Cantidad"}, data);

      pdfGenerator.setWriter(pdfWriter);
      pdfGenerator.setPdf(pdfDocument);
      pdfGenerator.setDocument(doc);

      boolean result = pdfGenerator.generatePdf();

      assertTrue(result, "generatePdf() retorna true");

      assertTrue(outputStream.size() > 0, "Se esperaba que se escribiera contenido en el PDF");

      doc.close();
      pdfDocument.close();
  }
}
