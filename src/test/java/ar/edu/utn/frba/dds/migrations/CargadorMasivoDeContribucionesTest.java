package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.models.entities.contribucion.Contribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CargadorMasivoDeContribucionesTest {
  private CargadorMasivoDeContribuciones contribuciones;

  @BeforeEach
  void setUp() throws IOException {
    contribuciones = new CargadorMasivoDeContribuciones(
        Paths.get("src", "test", "resources", "carga-masiva-contribuciones.csv")
    );
  }

  @Test
  void testCargaCSVEnIterador() {
    assertInstanceOf(Iterator.class, contribuciones);
  }

  @Test
  void testSoportaHasNext() {
    assertTrue(contribuciones.hasNext());
  }

  @Test
  void testPuedeLeerTodoElArchivo() {
    int cuenta;
    for (cuenta = 0; contribuciones.hasNext(); cuenta++) contribuciones.next();

    assertEquals(10, cuenta);
  }

  @Test
  void testPuedeProcesarLineas() {
    // DNI,30123456,Juan,Pérez,jperez@example.com,08/05/2024,DINERO,1000
    Dinero contribucion = (Dinero) contribuciones.next();
    Colaborador colaborador = contribucion.getColaborador();

    assertAll("Campos leídos",
        () -> assertInstanceOf(Dinero.class, contribucion),
        () -> assertEquals(TipoDocumento.DNI, colaborador.getDocumento().tipo()),
        () -> assertEquals(30123456, colaborador.getDocumento().valor()),
        () -> assertEquals("Juan", colaborador.getNombre()),
        () -> assertEquals("Pérez", colaborador.getApellido()),
        () -> {
          ContactoEmail email = (ContactoEmail) colaborador.getContactos().stream().findFirst().orElse(null);
          assertEquals(
              "jperez@example.com",
              email.destinatario()
          );
        },
        () -> assertEquals(
            ZonedDateTime.parse("2024-05-08T00:00-03:00[America/Argentina/Buenos_Aires]"),
            contribucion.getFecha()
        ),
        () -> assertEquals(1000, contribucion.getMonto())
    );
  }

  @Test
  void testIteradorSeReseteaDespuesDeCadaTest() {
    Contribucion contribucion = contribuciones.next();

    assertEquals(
        ZonedDateTime.parse("2024-05-08T00:00-03:00[America/Argentina/Buenos_Aires]"),
        contribucion.getFecha()
    );  // La fecha de la primera línea, de vuelta
  }

  @Test
  void testReciclaColaboradoresYaCreados() {
    while (contribuciones.hasNext()) contribuciones.next();

    assertEquals(7, contribuciones.getColaboradores().toArray().length);
  }
}