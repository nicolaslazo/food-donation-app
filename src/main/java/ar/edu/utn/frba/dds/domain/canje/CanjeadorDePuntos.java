package ar.edu.utn.frba.dds.domain.canje;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.contribucion.OfertaRecompensa;

public class CanjeadorDePuntos {
  public void canjearPuntos(Colaborador colaborador, OfertaRecompensa ofertaRecompensa) throws ExcepcionDeCanjeDePuntos {
    //Averiguar puntos
    // Ver si hay oferta
    // Actualizar oferta
    // Restar los puntos
    if (!(this.obtenerCostoPuntos(ofertaRecompensa) < this.obtenerPuntosColaborador(colaborador))) {
      throw new ExcepcionDeCanjeDePuntos("No hay puntos suficientes para realizar el canje");}

    if (!this.validarCantidad(ofertaRecompensa)) {
      throw new ExcepcionDeCanjeDePuntos("No hay más ofertas disponibles");}

    // Actualizamos los puntos del colaborador
    colaborador.actualizarPuntos(ofertaRecompensa.getCostoEnPuntos());
    //Actualiza cantidad de ofertas
    ofertaRecompensa.usarOferta();
    this.darDeBajaOferta(ofertaRecompensa);
  }

  private void darDeBajaOferta(OfertaRecompensa ofertaRecompensa) {
    //TODO ver lo que dicen los ayudantes :D
  }

  public double obtenerPuntosColaborador(Colaborador colaborador) {
    return colaborador.getCantidadDePuntos();
  }

  public double obtenerCostoPuntos(OfertaRecompensa ofertaRecompensa) {
    return ofertaRecompensa.getCostoEnPuntos();
  }

  public boolean validarCantidad (OfertaRecompensa ofertaRecompensa)
  {
    return (ofertaRecompensa.getCantidadDeOfertas() != 0);
  }

}

