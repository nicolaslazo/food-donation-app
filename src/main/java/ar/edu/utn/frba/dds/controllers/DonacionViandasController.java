package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.services.generadorPDF.PdfGenerator;
import ar.edu.utn.frba.dds.services.generadorPDF.ReporteViandasColaborador;

import java.util.Map;

public class DonacionViandasController {

  DonacionViandasRepository donacionViandasRepository;

  void generarReporte()
  {
    Map<String, Integer> datos = DonacionViandasRepository.calcularViandasPorColaboradorSemanaAnterior();
    PdfGenerator reporte = new ReporteViandasColaborador(datos);
    reporte.generatePdf();
  }


}
