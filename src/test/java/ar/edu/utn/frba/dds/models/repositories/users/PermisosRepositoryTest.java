package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermisosRepositoryTest {
  @AfterEach
  public void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testFindAllDevuelveTodosLosPermisosParaUnUsuario() {
    Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        null);
    Permiso permiso1 = new Permiso("Permiso-Custom1");
    Permiso permiso2 = new Permiso("Permiso-Custom2");
    Rol rol = new Rol("ROLCUSTOM", Set.of(permiso1, permiso2));

    new PermisosRepository().insertAll(List.of(permiso1, permiso2));
    new RolesRepository().insert(rol);

    colaborador.getUsuario().getRoles().add(rol);
    new ColaboradorRepository().insert(colaborador);

    assertEquals(
        List.of("Abrir-Heladera-Contribucion",
            "Donar-Viandas",
            "Asignar-Tarjetas",
            "Depositar-Viandas",
            "Permiso-Custom1",
            "Permiso-Custom2"),
        new PermisosRepository().findAll(colaborador.getUsuario()).map(Permiso::getNombre).toList());
  }
}
