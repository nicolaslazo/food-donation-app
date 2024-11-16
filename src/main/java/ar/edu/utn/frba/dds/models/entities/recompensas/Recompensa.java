package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.net.URL;

@Entity
@Table(name = "recompensa")
@ToString
@Getter
public class Recompensa {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @NonNull Long id;

  @Column(name = "nombre", nullable = false)
  @NonNull String nombre;

  @ManyToOne
  @JoinColumn(name = "idProveedor", referencedColumnName = "idUsuario", nullable = false, updatable = false)
  @NonNull Colaborador proveedor;

  @Column(name = "costoEnPuntos", nullable = false, updatable = false)
  @NonNull Double costoEnPuntos;

  @Column(name = "stockInicial", nullable = false, updatable = false)
  @NonNull Integer stockInicial;

  @Enumerated(EnumType.STRING)
  @Column(name = "rubro", nullable = false, updatable = false)
  @NonNull RubroRecompensa rubro;

  @Column(name = "imagen")
  URL imagen;

  public Recompensa(@NonNull String nombre,
                    @NonNull Colaborador proveedor,
                    @NonNull Double costoEnPuntos,
                    @NonNull Integer stockInicial,
                    @NonNull RubroRecompensa rubro,
                    URL imagen) {
    this.nombre = nombre;
    this.proveedor = proveedor;
    this.costoEnPuntos = costoEnPuntos;
    this.stockInicial = stockInicial;
    this.rubro = rubro;
    this.imagen = imagen;
  }

  protected Recompensa() {
  }
}
