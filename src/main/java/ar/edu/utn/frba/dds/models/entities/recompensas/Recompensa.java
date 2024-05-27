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
  @Setter
  private int id;
  @Getter
  private final int stockInicial;
  private final URL imagen;

  public Recompensa(@NonNull String nombre, @NonNull RubroRecompensa rubro, double costoEnPuntos, int stock, URL imagen) {
    this.costoEnPuntos = costoEnPuntos;
    this.nombre = nombre;
    this.rubro = rubro;
    this.stockInicial = stock;
    this.imagen = imagen;
  }
}
