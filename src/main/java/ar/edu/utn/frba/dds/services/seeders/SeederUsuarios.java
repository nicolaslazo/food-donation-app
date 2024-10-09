package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class SeederUsuarios {

  @PostConstruct
  public void seederUsuarios() {
    //TODO: Se deberian de incluir más colaboradores físicos y jurídicos. Tambien Técnicos.
    //No agregue mucho pq la PR de Registro cambian los constructores de usuarios, colaboradores y técnicos.
    Usuario usuarioAdmin = new Usuario(
            new Documento(TipoDocumento.DNI, 45111000),
            "Pepe",
            "Pepon",
            LocalDate.now().minusYears(20),
            DigestUtils.sha256Hex("pepe123"),
            new HashSet<>(Set.of(new RolesRepository().findByName("ADMINISTRADOR").get()))
    );

    Email emailUsuario = new Email(usuarioAdmin, "admin@gmail.com");
    new UsuariosRepository().insert(usuarioAdmin);
    new ContactosRepository().insert(emailUsuario);

    Colaborador colaboradorFisico = new Colaborador(
            new Documento(TipoDocumento.DNI, 45222000),
            "Osvaldo",
            "Benitez",
            LocalDate.now().minusYears(22),
            new CoordenadasGeograficas(54D, 54D),
            DigestUtils.sha256Hex("osvaldo123"),
            new RolesRepository().findByName("COLABORADORFISICO").get()
    );
    Email emailColaboradorFisico = new Email(colaboradorFisico.getUsuario(), "colaboradorFisico@gmail.com");
    new ColaboradorRepository().insert(colaboradorFisico);
    new ContactosRepository().insert(emailColaboradorFisico);
  }
}
