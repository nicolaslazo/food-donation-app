package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.CuidadoHeladeraInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.time.ZonedDateTime;

public class CuidadoHeladeraController {
  static HeladerasRepository heladerasRepository = new HeladerasRepository();

  public static void tomarCuidadoHeladera(String dtoCuidadoJson) throws RepositoryException {
    CuidadoHeladeraInputDTO dtoCuidado = CuidadoHeladeraInputDTO.desdeJson(dtoCuidadoJson);

    Colaborador encargado = new ColaboradorRepository()
        .findById(dtoCuidado.getIdColaborador())
        .orElseThrow();

    Heladera heladeraNueva = new Heladera(dtoCuidado.getNombreHeladera(),
        dtoCuidado.getUbicacion(),
        encargado,
        dtoCuidado.getCapacidadEnViandas(),
        ZonedDateTime.now());

    heladerasRepository.insert(heladeraNueva);
    SuscripcionRepository
        .getInstancia()
        .insert(new Suscripcion(heladeraNueva, MotivoDeDistribucion.FALLA_HELADERA, null, encargado));
  }
}
