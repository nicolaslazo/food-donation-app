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
  @NonNull
  private final int costoEnPuntos;

  private int cantidadOfertas;

  private URL imagen;

  public OfertaRecompensa(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, @NonNull String nombre,
                          @NonNull RubroRecompensa rubro, @NonNull int costoEnPuntos, URL imagen) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.nombre = nombre;
    this.rubro = rubro;
    this.costoEnPuntos = costoEnPuntos;
    this.imagen = imagen;
  }

  
}
