package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.controllers.heladera.HeladeraController;
import ar.edu.utn.frba.dds.dtos.input.heladera.SolicitudAperturaPorContribucionInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Mensaje;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.MensajeRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class SuscripcionController implements IMqttMessageListener {
  static SuscripcionController instancia = null;

  public static void suscribirAHeladera(Heladera heladera,
                                        MotivoDeDistribucion tipo,
                                        Integer parametro,
                                        Colaborador colaborador) {
    final double distanciaHabilitadaEnMetros =
        Double.parseDouble(ConfigLoader.getInstancia().getProperty("heladeras.suscripciones.radioHabilitadoEnMetros"));
    final double distancia = CalculadoraDistancia.calcular(heladera.getUbicacion(), colaborador.getUbicacion());

    if (distancia > distanciaHabilitadaEnMetros) {
      throw new RuntimeException("El colaborador vive demasiado lejos de esta heladera para poder suscribirse");
    }

    new SuscripcionRepository().insert(new Suscripcion(colaborador, heladera, tipo, parametro));
  }

  public static void notificarIncidente(Heladera heladera, ZonedDateTime fecha) {
    StringBuilder mensaje = new StringBuilder(
        String.format("Se detectó una falla en la heladera %s el %s. ", heladera.getNombre(), fecha.toString()));

    List<Heladera> destinosSugeridos = new HeladeraController().encontrarHeladerasCercanas(heladera);

    if (!destinosSugeridos.isEmpty()) {
      long cantidadViandasAfectadas = new ViandasRepository().findAll(heladera).count();
      mensaje.append(
          String.format(
              "De así desearlo, puede alocar las %s viandas afectadas a una de las siguientes heladeras:\n",
              cantidadViandasAfectadas));
    }

    for (Heladera sugerencia : destinosSugeridos)
      mensaje.append("\n\t* ").append(sugerencia.getNombre());

    new SuscripcionRepository()
        .findAll(heladera, MotivoDeDistribucion.FALLA_HELADERA)
        .map(Suscripcion::getColaborador)
        .map(Colaborador::getUsuario)
        .flatMap(a -> a.getContactos().stream()).toList()
        .forEach(contacto -> {
          try {
            contacto.enviarMensaje(mensaje.toString());
          } catch (MensajeAContactoException ignored) {
            return;
          } // TODO: Dónde podríamos loggear estas fallas?
        });
  }

  public static SuscripcionController getInstancia() {
    if (instancia == null) instancia = new SuscripcionController();

    return instancia;
  }

  /* Convierte un input DTO de solicitud de apertura en una instancia, garantizando que es válida */
  private static SolicitudAperturaPorContribucion verificarSolicitudEsProcesable(
      SolicitudAperturaPorContribucionInputDTO dtoSolicitudApertura) throws SolicitudInvalidaException {
    SolicitudAperturaPorContribucionRepository repositorioSolicitud = new SolicitudAperturaPorContribucionRepository();
    Optional<SolicitudAperturaPorContribucion> optionalSolicitudReferida = repositorioSolicitud
        .getSolicitudVigenteAlMomento(Long.valueOf(dtoSolicitudApertura.getId()),
            dtoSolicitudApertura.getEsExtraccion(),
            dtoSolicitudApertura.getFechaRealizada());

    if (optionalSolicitudReferida.isEmpty()) throw new SolicitudInvalidaException("Esto no debería pasar");

    return optionalSolicitudReferida.get();
  }

  private String construirMensajeDeNotificacion(Suscripcion suscripcion) {
    String formato = "Usted está siendo notificad@ porque está suscrit@ a la heladera \"%s\". " +
        "Se desea informarle que actualmente quedan %d %s.";

    Heladera heladera = suscripcion.getHeladera();
    long viandasDepositadas = new HeladerasRepository().getCantidadViandasDepositadas(heladera);

    String nombreHeladera = heladera.getNombre();
    long numero = switch (suscripcion.getTipo()) {
      case FALTAN_VIANDAS -> viandasDepositadas;
      case FALTA_ESPACIO -> heladera.getCapacidadEnViandas() - viandasDepositadas;
      default -> throw new RuntimeException("Esto no debería pasar");
    };
    String consecuencia = switch (suscripcion.getTipo()) {
      case FALTAN_VIANDAS -> "viandas para que la heladera quede vacía";
      case FALTA_ESPACIO -> "espacios para que la heladera se llene";
      default -> throw new RuntimeException("Esto no debería pasar");
    };

    return String.format(formato, nombreHeladera, numero, consecuencia);
  }

  /* Notifica a los interesados cuando el stock de una heladera sube o baja */
  @Override
  public void messageArrived(String topic, MqttMessage payload) throws SolicitudInvalidaException {
    SolicitudAperturaPorContribucionInputDTO confirmacionCambioEnStock =
        SolicitudAperturaPorContribucionInputDTO.desdeJson(payload.toString());

    SolicitudAperturaPorContribucion solicitudReferida = verificarSolicitudEsProcesable(confirmacionCambioEnStock);

    Heladera heladeraAfectada = confirmacionCambioEnStock.getEsExtraccion() ?
        solicitudReferida.getHeladeraOrigen().get() : solicitudReferida.getHeladeraDestino();

    ContactosRepository repositorioContactos = new ContactosRepository();
    MensajeRepository mensajeRepository = new MensajeRepository();

    new SuscripcionRepository()
        .findInteresadasEnStock(heladeraAfectada)
        .forEach(suscripcion -> {
          String mensajeAEnviar = construirMensajeDeNotificacion(suscripcion);

          repositorioContactos.get(suscripcion.getColaborador()).forEach(contacto -> {
            try {
              contacto.enviarMensaje(mensajeAEnviar);

              // Guardamos el mensaje en el repositorio después de enviarlo exitosamente
              Mensaje mensaje = new Mensaje(contacto, mensajeAEnviar, ZonedDateTime.now());
              mensajeRepository.insert(mensaje);

            } catch (MensajeAContactoException e) {
              // En caso de fallo al enviar el mensaje, lanzamos una excepción
              throw new RuntimeException(e);
            }
          });
        });
  }
}
