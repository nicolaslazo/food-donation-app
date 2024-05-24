package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.contribucion.Contribucion;
import ar.edu.utn.frba.dds.domain.contribucion.Dinero;
import ar.edu.utn.frba.dds.domain.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.domain.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.domain.contribucion.RedistribucionViandas;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.nio.file.Files.newBufferedReader;

/**
 * Itera línea por línea por un archivo de contribuciones ejecutadas
 * antes de la existencia del sistema.
 */
public class CargadorMasivoDeContribuciones implements Iterator<Contribucion> {
  private final Iterator<EntradaDeCargaCSV> reader;
  @Getter
  private final Set<Colaborador> colaboradores = new HashSet<>();

  public CargadorMasivoDeContribuciones(Path pathArchivoCsv) throws IOException {
    try (BufferedReader reader = newBufferedReader(pathArchivoCsv)) {
      CsvToBean<EntradaDeCargaCSV> csvToBean = new CsvToBeanBuilder<EntradaDeCargaCSV>(reader)
          .withType(EntradaDeCargaCSV.class)
          .build();
      // Idealmente llamaríamos a .iterator() directamente pero CsvToBean se comporta erráticamente con ese método
      this.reader = csvToBean.parse().iterator();
    }
  }

  private static List listaDeNulls(int cantidad) {
    return new ArrayList(Collections.nCopies(cantidad, null));
  }

  @Override
  public boolean hasNext() {
    return reader.hasNext();
  }

  private Colaborador crearColaborador(EntradaDeCargaCSV entrada) {
    Colaborador colaboradorNuevo = new Colaborador(entrada.getDocumento(), entrada.getNombre(), entrada.getApellido(), entrada.getMail());
    this.colaboradores.add(colaboradorNuevo);

    return colaboradorNuevo;
  }

  private Colaborador encontrarOCrearColaborador(EntradaDeCargaCSV entrada) {
    return this.colaboradores
        .stream()
        .filter(colaborador -> colaborador.getDocumento().equals(entrada.getDocumento()))
        .findAny()
        .orElse(crearColaborador(entrada));
  }

  private Contribucion instanciarContribucion(EntradaDeCargaCSV entrada, Colaborador colaborador) {
    final ZonedDateTime fecha = entrada.getFechaDeContribucion();

    return switch (entrada.getTipoDeContribucion()) {
      case "DINERO" -> new Dinero(colaborador, fecha, entrada.getCantidad(), null);
      case "DONACION_VIANDAS" -> new DonacionViandas(colaborador, fecha, listaDeNulls(entrada.getCantidad()));
      case "REDISTRIBUCION_VIANDAS" -> new RedistribucionViandas(
          colaborador,
          fecha,
          null,
          null,
          null,
          listaDeNulls(entrada.getCantidad()));
      case "ENTREGA_TARJETAS" -> new EntregaTarjetas(colaborador, fecha, listaDeNulls(entrada.getCantidad()));
      default -> throw new RuntimeException("Tipo de contribución no soportado");
    };
  }

  @Override
  public Contribucion next() {
    if (!this.hasNext()) throw new NoSuchElementException();

    EntradaDeCargaCSV lecturaNueva = reader.next();
    Colaborador colaborador = encontrarOCrearColaborador(lecturaNueva);

    return instanciarContribucion(lecturaNueva, colaborador);
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Por favor, proporcione la ruta del archivo CSV como argumento.");
      System.exit(1);
    }

    Path pathArchivoCsv = Paths.get(args[0]);

    try {
      Iterator<Contribucion> iterator = new CargadorMasivoDeContribuciones(pathArchivoCsv);

      while (iterator.hasNext()) {
        Contribucion contribucion = iterator.next();
        System.out.println(contribucion);
      }
    } catch (IOException e) {
      System.err.println("Error al leer el archivo CSV: " + e.getMessage());
    }
  }}
