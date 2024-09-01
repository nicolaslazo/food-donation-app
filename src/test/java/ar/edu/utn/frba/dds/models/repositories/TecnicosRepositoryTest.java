package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TecnicosRepositoryTest {
  TecnicoRepository repositorio = TecnicoRepository.getInstancia();
  Tecnico tecnico;

  @BeforeEach
  void setUp() {    
    tecnico = new Tecnico(
      new Documento(TipoDocumento.DNI, 123),
      "Lautaro",
      "velazquez",
      LocalDate.now(),
      "123456",
      new AreaGeografica( new CoordenadasGeograficas(0.0,0.0), 100)
    );
    repositorio.insert(tecnico);
  }

  @AfterEach
  void tearDown() {
    repositorio.deleteAll();
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(TecnicoRepository.class, repositorio);
  }

  @Test
  void insertarTecnicoSinFallar() {
    Assertions.assertTrue(repositorio.findAll().anyMatch(e -> e == tecnico));
  }

  @Test
  void obtenerTodosLostecnicos() {
    repositorio.insert(new Tecnico(
      new Documento(TipoDocumento.DNI, 123),
      "xxx",
      "xxx",
      LocalDate.now(),
      "401",
      new AreaGeografica( new CoordenadasGeograficas(0.0,0.0), 100)
    ));

    assertEquals(2, repositorio.findAll().count());
  }

  @Test
  void testGetTecnicoNoExistente() {
    Optional<Tecnico> encontrado = repositorio.searchBy("cuil", "999999");
    Assertions.assertFalse(encontrado.isPresent(), "El técnico no existe");
  }

  @Test
  void testGetTecnicoExistente() {
    repositorio.insert(tecnico);
    Optional<Tecnico> encontrado = repositorio.searchBy("cuil","123456");
    Assertions.assertTrue(encontrado.isPresent(), encontrado.get().toString() +
     "El técnico debería estar presente");
  }

  @Test
  void testDeleteTecnicoExistente() {
    repositorio.delete("123456");
    Optional<Tecnico> encontrado = repositorio.searchBy("cuil","123456");
    Assertions.assertFalse(encontrado.get().getUsuario().getActive(), "El técnico no debería estar presente después de ser eliminado");
  }

  @Test
  void testDeleteTodosLimpiaElRepositorio() {
    repositorio.deleteAll();
    assertTrue(repositorio.searchBy("cuil","123456").isEmpty());
  }
}