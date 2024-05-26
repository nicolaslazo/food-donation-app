package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.NonNull;

import java.net.URL;
import java.time.ZonedDateTime;

public class OfertaRecompensa extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @NonNull
  private final String nombre;
  @NonNull
  private final RubroRecompensa rubro;
  private final double costoEnPuntos;

  private int cantidadOfertas;

  private URL imagen;

  public OfertaRecompensa(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, @NonNull String nombre,
                          @NonNull RubroRecompensa rubro, @NonNull double costoEnPuntos, URL imagen) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.nombre = nombre;
    this.rubro = rubro;
    this.costoEnPuntos = costoEnPuntos;
    this.imagen = imagen;
  }

  public void usarOferta()
  {this.cantidadDeOfertas=-1;}
}

