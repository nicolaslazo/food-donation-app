package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.contribucion.Contribucion;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.newBufferedReader;

public class CargadorMasivoDeColaboradores implements Iterator<Contribucion> {
  private final Iterator<String[]> readerCSV;
  private final List<Colaborador> colaboradores = new ArrayList<>();

  public CargadorMasivoDeColaboradores(Path pathACSV) throws IOException {
    try (BufferedReader reader = newBufferedReader(pathACSV)) {
      readerCSV = new CSVReader(reader).iterator();
    }
  }

  @Override
  public boolean hasNext() {
    return readerCSV.hasNext();
  }

  private Colaborador crearColaborador(EntradaDeCargaCSV entrada) {
    Colaborador colaboradorNuevo = new Colaborador(entrada.documento(), entrada.nombre(), entrada.apellido());
    this.colaboradores.add(colaboradorNuevo);

    return colaboradorNuevo;
  }

  private Colaborador encontrarColaborador(EntradaDeCargaCSV entrada) {
    Colaborador retval = this.colaboradores.stream()
        .filter(colaborador ->
            (colaborador.documento.tipo == entrada.documento().tipo()) &&
                Objects.equals(colaborador.documento().valor(), entrada.documento().valor()))
        .findAny()
        .orElse(crearColaborador(entrada));

    return retval;
  }

  @Override
  public Contribucion next() {
    EntradaDeCargaCSV entradaNueva = EntradaDeCargaCSV.crearInstancia(this.readerCSV.next());
    // TODO: completar
    return null;
  }
}
