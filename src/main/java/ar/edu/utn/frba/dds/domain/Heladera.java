package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;
@Getter
@Setter
public class Heladera {
  private static final Logger logger = LoggerFactory.getLogger(Heladera.class);

  private String nombre;
  private Ubicacion ubicacion;
  private boolean enAlta;
  private int capacidadEnVianda;
  private ZonedDateTime fechaInstalacion;
  private List<Vianda> viandas;
  private double temperaturaMinima;
  private double temperaturaMaxima;
  private double ultimaTempRegistrada;
  private double temperaturaDeseada;
  private Colaborador colaboradorACargo;
  private boolean heladeraActiva;

  public Heladera() {
  }

  public Vianda sacarVianda() {
    // TODO implement here
    return null;
  }

  public void ingresarViandas(List<Vianda> viandas) {
    // TODO implement here
  }

  public void alertarMovimiento() {
    // TODO implement here
  }

  public void analizarEstadoHeladera(){
    //Si no se encuentra en una temperatura optima, la pone en estado inactiva
    if(!this.temperaturaOptimaParaFuncionamiento()) {
      //Deberiamos de loggear esto para que se sepa que fue por mala temperatura
      this.heladeraActiva = false;
      logger.warn(
              "Heladera {} desactivada debido a temperatura no óptima. Última temperatura registrada: {}",
              nombre, ultimaTempRegistrada);
    }
    //Si la heladera esta en estado desactivado entra al if
    if(!this.heladeraActiva) {
      //TODO deberiamos de notificar este cambio de estado
      logger.info("Heladera {} está actualmente inactiva.", nombre);
    }
  }

  public boolean temperaturaOptimaParaFuncionamiento(){
    return
            (this.temperaturaMaxima > this.ultimaTempRegistrada)
            &&
            (this.temperaturaMinima < this.ultimaTempRegistrada);
  }

  //Se recibe algun tipo de alerta de movimiento, todavia no sabemos de donde!
  public void alertaMovimientoDetectado() {
    //Entiendo que la alerta se envia al sistema, pero no afecta el estado de activa o no
    //TODO deberia de enviar una alerta al sistema
    logger.info("Alerta de movimiento detectada para la heladera {}.", nombre);
  }

}
