package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.CuidadoHeladeraInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.time.ZonedDateTime;

public class CuidadoHeladeraController {
  public static Heladera tomarCuidadoHeladera(String dtoCuidadoJson) {
    CuidadoHeladeraInputDTO dtoCuidado = CuidadoHeladeraInputDTO.desdeJson(dtoCuidadoJson);

    Colaborador encargado = new ColaboradorRepository()
        .findById(dtoCuidado.getIdColaborador())
        .orElseThrow();

    Heladera heladeraNueva = new Heladera(dtoCuidado.getNombreHeladera(),
        dtoCuidado.getUbicacion(),
        encargado,
        dtoCuidado.getCapacidadEnViandas(),
        ZonedDateTime.now(),
        dtoCuidado.getBarrio());

    new HeladerasRepository().insert(heladeraNueva);
    new SuscripcionRepository()
        .insert(new Suscripcion(encargado, heladeraNueva, MotivoDeDistribucion.FALLA_HELADERA, null));

    return heladeraNueva;
  }
}
