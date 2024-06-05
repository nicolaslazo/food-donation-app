package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class TecnicosRepositoryTest {
  TecnicoRepository repositorio;
  Tecnico tecnico;

  @BeforeEach
  void setUp() {
    repositorio = new TecnicoRepository();
    tecnico = new Tecnico("123456");
    Tecnico tecnico1 = new Tecnico("401");
    Tecnico tecnico2 = new Tecnico("402");
    repositorio.insertTecnico(tecnico1);
    repositorio.insertTecnico(tecnico2);
  }


  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(TecnicoRepository.class, repositorio);
  }

  @Test
  void insertarTecnicoSinFallar() {
    repositorio.insertTecnico(tecnico);
    Assertions.assertTrue(repositorio.getTecnicos().contains(tecnico));
  }

  @Test
  void obtenerTodosLostecnicos() {
    repositorio.insertTecnico(tecnico);

    assertEquals(3, repositorio.getTecnicos().size());
  }

  @Test
  void testGetTecnicoNoExistente() {
    Optional<Tecnico> encontrado = repositorio.getTecnico("999999");
    Assertions.assertFalse(encontrado.isPresent(), "El técnico no existe");
  }

  @Test
  void testGetTecnicoExistente() {
    repositorio.insertTecnico(tecnico);


    Optional<Tecnico> encontrado = repositorio.getTecnico("123456");
    Assertions.assertTrue(encontrado.isPresent(), "El técnico debería estar presente");
    assertEquals(tecnico, encontrado.get(), "técnico encontrado");
  }

  @Test
  void testDeleteTecnicoExistente() {
    repositorio.insertTecnico(tecnico);
    boolean resultado = repositorio.deleteTecnico("123456");
    Assertions.assertTrue(resultado, "El técnico debería haber sido eliminado");
    Optional<Tecnico> encontrado = repositorio.getTecnico("123456");
    Assertions.assertFalse(encontrado.isPresent(), "El técnico no debería estar presente después de ser eliminado");
  }

  @Test
  void testDeleteTecnicoNoExistente() {
    boolean resultado = repositorio.deleteTecnico("999999");
    Assertions.assertFalse(resultado, "El técnico no debería haber sido eliminado porque no existe");
  }
}