package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TecnicosRepositoryTest {
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);

  Tecnico tecnico = new Tecnico(
          new Documento(TipoDocumento.DNI, 123),
          "Primero",
          "Primero",
          LocalDate.now(),
          "1",
          new AreaGeografica( obelisco, 100)
  );

  Tecnico tecnicoDos = new Tecnico(
          new Documento(TipoDocumento.DNI, 321),
          "Segundo",
          "Segundo",
          LocalDate.now(),
          "2",
          new AreaGeografica(obelisco, 50)
  );

  @BeforeEach
  void setUp() {
    new TecnicoRepository().insert(tecnico);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void buscarPorId() {
    Optional<Tecnico> encontrado = new TecnicoRepository().findById(tecnico.getId());
    assertTrue(encontrado.isPresent());
    assertEquals(tecnico.getId(), encontrado.get().getId());
  }

  @Test
  void obtenerTodosLostecnicos() {
    new TecnicoRepository().insert(tecnicoDos);
    Stream<Tecnico> encontrados = new TecnicoRepository().findAll();

    assertEquals(2, encontrados.count());
  }

  @Test
  void testGetTecnicoNoExistente() {
    new TecnicoRepository().delete(tecnico);
    Optional<Tecnico> noEncontrado = new TecnicoRepository().findById(tecnico.getId());
    assertTrue(noEncontrado.isEmpty());
  }

  @Test
  void testDeleteTodosLosTecnicos() {
    new TecnicoRepository().deleteAll();
    Stream<Tecnico> vacio = new TecnicoRepository().findAll();

    assertEquals(0, vacio.count());
  }
}