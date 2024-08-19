package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contribucion.Contribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Optional;

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
        () -> assertEquals(TipoDocumento.DNI, colaborador.getUsuario().getDocumento().tipo()),
        () -> assertEquals(30123456, colaborador.getUsuario().getDocumento().valor()),
        () -> assertEquals("Juan", colaborador.getUsuario().getPrimerNombre()),
        () -> assertEquals("Pérez", colaborador.getUsuario().getApellido()),
        () -> {
          Optional<Contacto> contacto = colaborador.getContactos().stream().findFirst();
          Email email = (Email) contacto.get();

          assertEquals(
              "jperez@example.com",
              email.destinatario()
          );
        },
        () -> assertEquals(
            ZonedDateTime.parse("2024-05-08T00:00-03:00[America/Argentina/Buenos_Aires]"),
            contribucion.getFechaRealizada()
        ),
        () -> assertEquals(1000, contribucion.getMonto())
    );
  }

  @Test
  void testIteradorSeReseteaDespuesDeCadaTest() {
    Contribucion contribucion = contribuciones.next();

    assertEquals(
        ZonedDateTime.parse("2024-05-08T00:00-03:00[America/Argentina/Buenos_Aires]"),
        contribucion.getFechaRealizada()
    );  // La fecha de la primera línea, de vuelta
  }

  @Test
  void testReciclaColaboradoresYaCreados() {
    while (contribuciones.hasNext()) contribuciones.next();

    assertEquals(7, contribuciones.getColaboradores().size());
  }
}