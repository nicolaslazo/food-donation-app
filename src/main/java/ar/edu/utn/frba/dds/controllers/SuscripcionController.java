package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.controllers.heladera.HeladeraController;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;

import java.time.ZonedDateTime;
import java.util.List;

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

  public static void notificarIncidente(Heladera heladera, ZonedDateTime fecha) {
    // TODO: Checkear que la heladera tenga viandas antes de tirar notificación

    StringBuilder mensaje =
        new StringBuilder(
            String.format("Se detectó una falla en la heladera %s el %s. ", heladera.getNombre(), fecha.toString()));

    List<Heladera> destinosSugeridos = new HeladeraController().encontrarHeladerasCercanas(heladera);

    if (!destinosSugeridos.isEmpty())
      mensaje.append("De así desearlo, puede alocar las viandas afectadas a una de las siguientes heladeras:\n");

    for (Heladera sugerencia : destinosSugeridos) mensaje.append("\n\t* ").append(sugerencia.getNombre());

    SuscripcionRepository
        .getInstancia()
        .getTodas(heladera, MotivoDeDistribucion.FALLA_HELADERA)
        .stream()
        .map(Suscripcion::getColaborador)
        .forEach(colaborador -> colaborador.enviarMensaje(mensaje.toString()));
  }
}
