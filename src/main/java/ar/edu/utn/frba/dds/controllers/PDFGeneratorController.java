package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.MovimientosHeladeraRepository;

import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.services.generadorpdf.PdfGenerator;

import java.util.HashMap;
import java.util.Map;

public class PDFGeneratorController {
  public static void main(String[] args) {
    //PDFGeneratorController.generarReporteMovimientoHeladeras();
    PDFGeneratorController.generarReporteFallasHeladeras();
    PDFGeneratorController.generarReporteViandasColaborador();
  }

  /*static void generarReporteMovimientoHeladeras() {
    MovimientosHeladeraRepository movimientosHeladeraRepository = MovimientosHeladeraRepository.getInstancia();

    Map<Heladera, Long> cantidadPorHeladera =
        movimientosHeladeraRepository.getCantidadMovimientosPorHeladeraSemanaAnterior();
    Map<String, Long> cantidadPorNombreDeHeladera = new HashMap<>();

    for (Map.Entry<Heladera, Long> entrada : cantidadPorHeladera.entrySet())
      cantidadPorNombreDeHeladera.put(entrada.getKey().getNombre(), entrada.getValue());

    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteMovimientosHeladera.pdf",
        "Cantidad de heladeras retiradas y colocadas por heladera",
        new String[]{"HELADERA", "CANTIDAD"},
        cantidadPorNombreDeHeladera);
    generador.generatePdf();
  }*/

  static void generarReporteFallasHeladeras() {
    Map<Heladera, Long> cantidadPorHeladera =
        new IncidenteRepository().findCantidadIncidentesPorHeladeraSemanaPasada();
    Map<String, Long> cantidadPorNombreDeHeladera = new HashMap<>();

    for (Map.Entry<Heladera, Long> entrada : cantidadPorHeladera.entrySet())
      cantidadPorNombreDeHeladera.put(entrada.getKey().getNombre(), entrada.getValue());

    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteFallasHeladera.pdf",
        "Cantidad de fallas por heladera",
        new String[]{"HELADERA", "CANTIDAD"},
        cantidadPorNombreDeHeladera);
    generador.generatePdf();
  }

  static void generarReporteViandasColaborador() {
    Map<Colaborador, Long> cantidadPorColaborador =
        DonacionViandasRepository.getInstancia().getCantidadDonacionesPorColaboradorSemanaAnterior();
    Map<String, Long> cantidadPorNombreDeColaborador = new HashMap<>();

    for (Map.Entry<Colaborador, Long> entrada : cantidadPorColaborador.entrySet())
      cantidadPorNombreDeColaborador.put(entrada.getKey().getNombreCompleto(), entrada.getValue());

    PdfGenerator generador = new PdfGenerator("src/main/reportes/ReporteViandasPorColaborador.pdf",
        "Cantidad de viandas donadas por colaborador",
        new String[]{"COLABORADOR", "CANTIDAD"},
        cantidadPorNombreDeColaborador);
    generador.generatePdf();
  }
}