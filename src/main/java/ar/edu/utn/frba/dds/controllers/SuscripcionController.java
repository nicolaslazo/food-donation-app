package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contacto.TipoNotificacion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;

import java.util.Set;

public class SuscripcionController {
  public static void suscribirAHeladera(Heladera heladera,
                                        Set<TipoNotificacion> notificaciones,
                                        Colaborador colaborador) {
    double distanciaHabilitadaEnMetros =
        Double.parseDouble(ConfigLoader.getInstancia().getProperty("heladeras.suscripciones.radioHabilitadoEnMetros"));
    double distancia = CalculadoraDistancia.calcular(heladera.getUbicacion(), colaborador.getUbicacion());

    if (distancia > distanciaHabilitadaEnMetros) {
      throw new RuntimeException("El colaborador vive demasiado lejos de esta heladera para poder suscribirse");
    }

    try {
      SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();

      for (TipoNotificacion tipo : notificaciones) repositorio.insert(new Suscripcion(heladera, tipo, colaborador));
    } catch (RepositoryInsertException e) {
      throw new RuntimeException(e);
    }
  }
}
