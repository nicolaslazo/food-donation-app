package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RolesRepositoryTest {
  RolesRepository rolesRepository = new RolesRepository();
  Rol rol;

  @BeforeEach
  public void setUp() {
    rol = new Rol("ADMINISTRADOR");
    rolesRepository.insert(rol);
  }

  @AfterEach
  public void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testFindByName() {
    Optional<Rol> rolEncontrado = rolesRepository.findByName("ADMINISTRADOR");
    assertTrue(rolEncontrado.isPresent());
    assertEquals(rol.getId(), rolEncontrado.get().getId());
  }
}
