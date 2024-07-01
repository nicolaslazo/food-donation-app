package ar.edu.utn.frba.dds.services.generadorPDF;

import ar.edu.utn.frba.dds.controllers.DonacionViandasController;
import ar.edu.utn.frba.dds.controllers.heladera.MovimientosHeladeraController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Map;

public class ServicePDFGenerator {

  private final DonacionViandasController donacionViandasController;

  private final MovimientosHeladeraController movimientosHeladeraController;

  private final IncidenteController incidenteController;

  public ServicePDFGenerator(DonacionViandasController donacionViandasService, MovimientosHeladeraController movimientosHeladeraService, IncidenteController incidenteController) {
    this.donacionViandasController = donacionViandasService;
    this.movimientosHeladeraController = movimientosHeladeraService;
    this.incidenteController = incidenteController;
  }

  void GenerarReporteFallasHeladeras() {
    Map<String,Integer> data = incidenteController.obtenerFallasHeladeraSemanaAnterior();
     PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteFallasHeladera.pdf",
       "Cantidad de fallas por heladera", new String[]{"HELADERA", "CANTIDAD"}, data) ;
     generador.generatePdf();
  }

  void GenerarReporteMovimientoHeladeras() {
    Map<String, Integer> data = movimientosHeladeraController.obtenerCantidadMovimientosHeladeraSemanaAnterior();
    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteMovimientosHeladera.pdf",
        "Cantidad de heladeras retiradas y colocadas por heladera", new String[]{"HELADERA", "CANTIDAD"}, data);
    generador.generatePdf();
  }

  void GenerarReporteViandasColaborador() {
    Map<String, Integer> data = donacionViandasController.obtenerDonacionesPorColaboradorSemanaAnterior();
    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteViandasPorColaborador.pdf",
        "Cantidad de viandas donadas por colaborador", new String[]{"COLABORADOR", "CANTIDAD"}, data);
    generador.generatePdf();
  }
  public static void main(String[] args) {
    // Crear instancias de los servicios necesarios
    DonacionViandasController donacionViandas = new DonacionViandasController();
    MovimientosHeladeraController movimientosHeladera = new MovimientosHeladeraController();
    IncidenteController incidenteController = null;
    try {
      incidenteController = new IncidenteController();
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }

    // Crear instancia del generador de PDF
    ServicePDFGenerator pdfGenerator = new ServicePDFGenerator(donacionViandas, movimientosHeladera, incidenteController);

    // Generar cada reporte llamando a los métodos correspondientes
    pdfGenerator.GenerarReporteFallasHeladeras();
    pdfGenerator.GenerarReporteMovimientoHeladeras();
    pdfGenerator.GenerarReporteViandasColaborador();
  }
}