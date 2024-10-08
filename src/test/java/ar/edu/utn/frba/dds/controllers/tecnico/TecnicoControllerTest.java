package ar.edu.utn.frba.dds.controllers.tecnico;

import ar.edu.utn.frba.dds.dtos.input.tecnico.TecnicoInputDTO;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TecnicoControllerTest {
  TecnicoController tecnicoController = new TecnicoController();
  TecnicoRepository tecnicoRepository = new TecnicoRepository();

  // JSON del TecnicoInputDTO
  String jsonDataTecnico = """
      {
          "tipoDocumento": "DNI",
          "numeroDocumento": 123,
          "primerNombre": "Primero",
          "apellido": "Primero",
          "fechaNacimiento": "2024-09-27",
          "cuil": "101239",
          "latitud": -34.5609872,
          "longitud": -58.501046,
          "radioEnMetros": 100.0,
          "contrasenia": "pepe123"
      }
      """;

  Usuario admin;

  @BeforeEach
  public void setUp() {
    new SeederRoles().seedRoles();
    admin = new Usuario(
        new Documento(TipoDocumento.DNI, 321),
        "admin",
        "admin",
        LocalDate.now(),
        null,
        new HashSet<>(List.of(new RolesRepository().findByName("ADMINISTRADOR").get()))
    );
  }

  @AfterEach
  public void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  public void testCrearTecnico() throws PermisoDenegadoException {
    // Obtener el DTO desde el JSON
    TecnicoInputDTO dataTecnico = TecnicoInputDTO.desdeJson(jsonDataTecnico);

    tecnicoController.crearTecnico(dataTecnico, admin);
    Stream<Tecnico> tecnicos = tecnicoRepository.findAll();
    assertEquals(1, tecnicos.count());
  }

  @Test
  public void testFallaCreacionPorNoTenerPermiso() {
    TecnicoInputDTO dataTecnico = TecnicoInputDTO.desdeJson(jsonDataTecnico);
    Usuario usuarioDummy = new Usuario(
        new Documento(TipoDocumento.DNI, 12345),
        "user",
        "dummy",
        LocalDate.now(),
        null,
        new HashSet<>(List.of(new RolesRepository().findByName("TECNICO").get()))
    );
    assertThrows(PermisoDenegadoException.class, () ->
        tecnicoController.crearTecnico(dataTecnico, usuarioDummy));
  }
}
