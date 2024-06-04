package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;

public class Recompensa {
  @Getter
  final double costoEnPuntos;
  @NonNull
  private final String nombre;
  @NonNull
  private final RubroRecompensa rubro;
  @Getter
  private final int stockInicial;
  private final URL imagen;
  @Getter
  @Setter
  private int id;

  public Recompensa(@NonNull String nombre, @NonNull RubroRecompensa rubro, double costoEnPuntos, int stockInicial, URL imagen) {
    this.costoEnPuntos = costoEnPuntos;
    this.nombre = nombre;
    this.rubro = rubro;
    this.stockInicial = stockInicial;
    this.imagen = imagen;
  }
}
