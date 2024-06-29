package ar.edu.utn.frba.dds.services.generadorPDF;

import ar.edu.utn.frba.dds.models.internalServices.DonacionViandasService;
import ar.edu.utn.frba.dds.models.internalServices.MovimientosHeladeraService;

import java.util.Map;

public class ServicePDFGenerator {

  private final DonacionViandasService donacionViandasService;

  private final MovimientosHeladeraService movimientosHeladeraService;

  public ServicePDFGenerator(DonacionViandasService donacionViandasService, MovimientosHeladeraService movimientosHeladeraService) {
    this.donacionViandasService = donacionViandasService;
    this.movimientosHeladeraService = movimientosHeladeraService;
  }

  void GenerarReporteFallasHeladeras() {
    //
    //PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteFallasHeladera.pdf",
    //    "Cantidad de fallas por heladera", new String[]{"HELADERA", "CANTIDAD"}, data)
    //generador.generatePdf();
  }

  void GenerarReporteMovimientoHeladeras() {
    Map<String, Integer> data = movimientosHeladeraService.obtenerCantidadMovimientosPorHeladeraSemanaAnterior();
    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteMovimientosHeladera.pdf",
        "Cantidad de heladeras retiradas y colocadas por heladera", new String[]{"HELADERA", "CANTIDAD"}, data);
    generador.generatePdf();
  }

  void GenerarReporteViandasColaborador() {
    Map<String, Integer> data = donacionViandasService.obtenerDonacionesPorColaboradorSemanaAnterior();
    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteViandasPorColaborador.pdf",
        "Cantidad de viandas donadas por colaborador", new String[]{"COLABORADOR", "CANTIDAD"}, data);
    generador.generatePdf();
  }
  public static void main(String[] args) {
    // Crear instancias de los servicios necesarios
    DonacionViandasService donacionViandasService = new DonacionViandasService();
    MovimientosHeladeraService movimientosHeladeraService = new MovimientosHeladeraService();

    // Crear instancia del generador de PDF
    ServicePDFGenerator pdfGenerator = new ServicePDFGenerator(donacionViandasService, movimientosHeladeraService);

    // Generar cada reporte llamando a los métodos correspondientes
    pdfGenerator.GenerarReporteFallasHeladeras();
    pdfGenerator.GenerarReporteMovimientoHeladeras();
    pdfGenerator.GenerarReporteViandasColaborador();
  }
}