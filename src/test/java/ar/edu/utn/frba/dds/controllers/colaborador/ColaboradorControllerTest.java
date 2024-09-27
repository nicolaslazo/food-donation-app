package ar.edu.utn.frba.dds.controllers.colaborador;

import ar.edu.utn.frba.dds.dtos.input.colaborador.ColaboradorInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.services.SeederRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ColaboradorControllerTest {
    ColaboradorController colaboradorController = new ColaboradorController();
    ColaboradorRepository colaboradorRepository = new ColaboradorRepository();
    ColaboradorInputDTO dataColaborador = new ColaboradorInputDTO(
            "DNI",
            123,
            "Primero",
            "Primero",
            LocalDate.now(),
            -34.5609872,
            -58.501046
    );

    Usuario admin;

    @BeforeEach
    public void setUp() {
        new SeederRoles().seedRoles();
        admin = new Usuario(
                new Documento(TipoDocumento.DNI, 321),
                "admin",
                "admin",
                LocalDate.now(),
                Set.of(new RolesRepository().findByName("ADMINISTRADOR").get())
        );
    }

    @AfterEach
    public void tearDown() {
        new HibernatePersistenceReset().execute();
    }

    @Test
    public void testCrearColaborador() throws PermisoDenegadoException {
        colaboradorController.crearColaborador(dataColaborador,admin);
        Stream<Colaborador> colaboradores = colaboradorRepository.findAll();
        assertEquals(1, colaboradores.count());
    }

    @Test
    public void testFallaCreacionPorNoTenerPermiso() throws PermisoDenegadoException {
        Usuario usuarioDummy = new Usuario(
                new Documento(TipoDocumento.DNI, 12345),
                "user",
                "dummy",
                LocalDate.now(),
                Set.of(new RolesRepository().findByName("TECNICO").get())
        );
        assertThrows(PermisoDenegadoException.class, () ->
                colaboradorController.crearColaborador(dataColaborador,usuarioDummy));
    }
}
