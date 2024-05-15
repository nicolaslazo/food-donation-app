package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.contribucion.ContribucionLegacy;
import ar.edu.utn.frba.dds.contribucion.TipoContribucion;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.domain.documentacion.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CargadorMasivoDeContribucionesTest {
  private Iterator<ContribucionLegacy> contribuciones;

  @BeforeEach
  void setUp() throws IOException {
    contribuciones = new CargadorMasivoDeContribuciones(
        Paths.get("src", "test", "resources", "carga-masiva-colaboradores.csv")
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
  void testSoportaSegundoHasNext() {
    // Test medio específico, pero es un problema que tuve
    contribuciones.next();

    assertTrue(contribuciones.hasNext());
  }

  @Test
  void testPuedeLeerTodoElArchivo() {
    int cuenta;

    for (cuenta = 0; contribuciones.hasNext(); cuenta++) {
      contribuciones.next();
    }

    assertEquals(10, cuenta);
  }

  @Test
  void testPuedeProcesarLineas() {
    // DNI,30123456,Juan,Pérez,jperez@example.com,08/05/2024,DINERO,1000
    ContribucionLegacy contribucion = contribuciones.next();
    Colaborador colaborador = contribucion.colaborador();

    assertAll("Campos leídos",
        () -> assertEquals(TipoContribucion.DINERO, contribucion.tipo()),
        () -> assertEquals(TipoDocumento.DNI, colaborador.getDocumento().tipo()),
        () -> assertEquals(30123456, colaborador.getDocumento().valor()),
        () -> assertEquals("Juan", colaborador.getNombre()),
        () -> assertEquals("Pérez", colaborador.getApellido()),
        () -> {
          ContactoEmail email = (ContactoEmail) colaborador.getContactos().stream().findFirst().get();
          assertEquals(
              "jperez@example.com",
              email.destinatario()
          );
        },
        () -> assertEquals(LocalDate.parse("2024-05-08"), contribucion.fecha()),
        () -> assertEquals(1000, contribucion.cantidad())
    );
  }

  @Test
  void testIteradorSeReseteaDespuesDeCadaTest() {
    ContribucionLegacy contribucion = contribuciones.next();

    assertEquals(LocalDate.parse("2024-05-08"), contribucion.fecha());  // La fecha de la primera línea, de vuelta
  }

  @Test
  void testRegistraContribucionesEnColaborador() {
    ContribucionLegacy contribucion = contribuciones.next();

    assertEquals(contribucion.cantidad(), contribucion.colaborador().getDineroDonado());
  }

  @Test
  void testReciclaColaboradoresYaCreados() {
    // DNI,30123456,Juan,Pérez,jperez@example.com,08/05/2024,DINERO,1000
    // DNI,30123456,Juan,Pérez,jperez@example.com,08/15/2024,DONACION_VIANDAS,15

    ContribucionLegacy primeraContribucion = contribuciones.next();
    ContribucionLegacy segundaContribucion = contribuciones.next();

    assertEquals(primeraContribucion.colaborador(), segundaContribucion.colaborador());
    assertEquals(15, primeraContribucion.colaborador().getViandasDonadas());
    assertEquals(1000, segundaContribucion.colaborador().getDineroDonado());
  }
}