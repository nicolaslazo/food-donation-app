package ar.edu.utn.frba.dds.services.generadorPDF;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

public class PdfGeneratorTest {

  private PdfWriter mockWriter;
  private PdfDocument mockPdf;
  private Document mockDocument;

  @BeforeEach
  public void setUp() {
    mockWriter = Mockito.mock(PdfWriter.class);
    mockPdf = Mockito.mock(PdfDocument.class);
    mockDocument = Mockito.mock(Document.class);
  }

  @Test
  public void testInicializarPDF()  {
    PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
        new String[]{"Heladera", "Cantidad"}, new HashMap<>(), mockWriter, mockPdf, mockDocument);

    pdfGenerator.generatePdf();

    Mockito.verify(mockDocument).close();
    assertTrue(pdfGenerator.generatePdf(), "generatePdf() retorna true");
  }

  @Test
  public void testAgregarTitulo() {
    Document mockDocument = Mockito.mock(Document.class);

    PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
        new String[]{"Heladera", "Cantidad"}, new HashMap<>(), null, null, mockDocument);

    pdfGenerator.agregarTitulo();

    Mockito.verify(mockDocument, times(1)).add(Mockito.any(Paragraph.class));
  }

  @Test
  public void testAgregarFecha() {
    Document mockDocument = Mockito.mock(Document.class);

    PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
        new String[]{"Heladera", "Cantidad"}, new HashMap<>(), null, null, mockDocument);

    pdfGenerator.agregarFecha();

    Mockito.verify(mockDocument, times(1)).add(Mockito.any(Paragraph.class));
  }

  @Test
  public void testAgregarTabla() {
    Document mockDocument = Mockito.mock(Document.class);
    PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
        new String[]{"Heladera", "Cantidad"}, new HashMap<>(), null, null, mockDocument);

    pdfGenerator.agregarTabla();

    Mockito.verify(mockDocument).add(Mockito.any(Table.class));
  }

  @Test
  public void testGeneratePdf() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PdfWriter pdfWriter = new PdfWriter(outputStream);


    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
    Document document = new Document(pdfDocument);

    Map<String, Integer> data = new HashMap<>();
    data.put("Heladera A", 10);
    data.put("Heladera B", 20);

    PdfGenerator pdfGenerator = new PdfGenerator("prueba.pdf", "Tabla de Prueba",
        new String[]{"Heladera", "Cantidad"}, data, pdfWriter, pdfDocument, document);

    boolean result = pdfGenerator.generatePdf();

    assertTrue(result, "generatePdf() retorna true");

    assertTrue(outputStream.size() > 0, "Se esperaba que se escribiera contenido en el PDF");

    document.close();
    pdfDocument.close();
  }
}
