package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViandasRepositoryTest {
  final ViandasRepository repositorio = new ViandasRepository();
  final Heladera heladeraMock = Mockito.mock(Heladera.class);
  final Vianda viandaMock = Mockito.mock(Vianda.class);
  final Vianda otraViandaMock = Mockito.mock(Vianda.class);  // .getHeladeraDestino() devuelve null por default

  @BeforeEach
  void setUp() {
    repositorio.deleteTodas();

    Mockito.when(heladeraMock.getCapacidadEnViandas()).thenReturn(1);
  }

  @Test
  void testInsertFallaSiHeladeraNoTieneEspacio() throws RepositoryException {
    Mockito.when(viandaMock.getHeladera()).thenReturn(heladeraMock);

    repositorio.insert(viandaMock);  // Heladera ahora está llena

    assertThrows(RepositoryException.class, () -> repositorio.insert(viandaMock));
  }

  @Test
  void testInsertDeCollectionFallaSiViandasTienenHeladerasDistintas() {
    Mockito.when(viandaMock.getHeladera()).thenReturn(heladeraMock);

    assertThrows(RepositoryException.class, () -> repositorio.insert(List.of(otraViandaMock, viandaMock)));
  }

  @Test
  void testInsertDeCollectionFallaSiHayDemasiadasViandas() {
    Mockito.when(viandaMock.getHeladera()).thenReturn(heladeraMock);
    Mockito.when(otraViandaMock.getHeladera()).thenReturn(heladeraMock);

    assertThrows(RepositoryException.class, () -> repositorio.insert(List.of(viandaMock, otraViandaMock)));
    assertEquals(0, repositorio.findAll().count());
  }
}