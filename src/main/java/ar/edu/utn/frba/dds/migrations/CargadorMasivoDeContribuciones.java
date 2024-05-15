package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.contribucion.ContribucionLegacy;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.nio.file.Files.newBufferedReader;

/**
 * Itera línea por línea por un archivo de contribuciones ejecutadas antes de la existencia del sistema
 */
public class CargadorMasivoDeContribuciones implements Iterator<ContribucionLegacy> {
  private final Iterator<EntradaDeCargaCSV> reader;
  private final List<Colaborador> colaboradores = new ArrayList<>();

  public CargadorMasivoDeContribuciones(Path pathACSV) throws IOException {
    try (BufferedReader reader = newBufferedReader(pathACSV)) {
      CsvToBean<EntradaDeCargaCSV> csvToBean = new CsvToBeanBuilder<EntradaDeCargaCSV>(reader)
          .withType(EntradaDeCargaCSV.class)
          .build();
      this.reader = csvToBean.iterator();
    }
  }

  @Override
  public boolean hasNext() {
    return reader.hasNext();
  }

  private Colaborador crearColaborador(EntradaDeCargaCSV entrada) {
    Colaborador colaboradorNuevo = new Colaborador(
        entrada.getDocumento(),
        entrada.getNombre(),
        entrada.getApellido(),
        entrada.getMail()
    );
    this.colaboradores.add(colaboradorNuevo);

    return colaboradorNuevo;
  }

  private Colaborador encontrarOCrearColaborador(EntradaDeCargaCSV entrada) {
    return this.colaboradores
        .stream()
        .filter(colaborador -> colaborador.getDocumento() == entrada.getDocumento())
        .findAny()
        .orElse(crearColaborador(entrada));
  }

  @Override
  public ContribucionLegacy next() {
    if (!this.hasNext()) throw new NoSuchElementException();

    EntradaDeCargaCSV lecturaNueva = reader.next();
    Colaborador colaborador = encontrarOCrearColaborador(lecturaNueva);

    return new ContribucionLegacy(
        lecturaNueva.getTipoDeContribucion(),
        colaborador,
        lecturaNueva.getFechaDeContribucion(),
        lecturaNueva.getCantidad()
    );
  }
}