package ar.edu.utn.frba.dds.domain.recompensas;

import ar.edu.utn.frba.dds.domain.contribucion.RubroRecompensa;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;

public class Recompensa {
  @Getter
  @Setter
  private int id;
  @Getter
  final double costoEnPuntos;
  @NonNull
  private final String nombre;
  @NonNull
  private final RubroRecompensa rubro;
  @Getter
  private int stockInicial;
  private URL imagen;

  public Recompensa(@NonNull String nombre, @NonNull RubroRecompensa rubro, double costoEnPuntos, int stock, URL imagen) {
    this.costoEnPuntos = costoEnPuntos;
    this.nombre = nombre;
    this.rubro = rubro;
    this.stockInicial = stock;
    this.imagen = imagen;
  }
}
