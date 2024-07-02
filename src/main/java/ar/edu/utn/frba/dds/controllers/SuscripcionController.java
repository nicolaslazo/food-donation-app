package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;

public class SuscripcionController {
  public static void suscribirAHeladera(Heladera heladera,
                                        MotivoDeDistribucion tipo,
                                        Integer parametro,
                                        Colaborador colaborador) throws RepositoryException {
    final double distanciaHabilitadaEnMetros =
        Double.parseDouble(ConfigLoader.getInstancia().getProperty("heladeras.suscripciones.radioHabilitadoEnMetros"));
    final double distancia = CalculadoraDistancia.calcular(heladera.getUbicacion(), colaborador.getUbicacion());

    if (distancia > distanciaHabilitadaEnMetros) {
      throw new RuntimeException("El colaborador vive demasiado lejos de esta heladera para poder suscribirse");
    }

    SuscripcionRepository.getInstancia().insert(new Suscripcion(heladera, tipo, parametro, colaborador));
  }
}
