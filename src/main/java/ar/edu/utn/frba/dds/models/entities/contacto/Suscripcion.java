package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "suscripcion",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"idColaborador", "idHeladera", "tipo"})})
@Getter
@ToString
public class Suscripcion {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true, updatable = false)
  Long id;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Colaborador.class)
  @JoinColumn(name = "idColaborador", referencedColumnName = "idUsuario", nullable = false, updatable = false)
  @NonNull Colaborador colaborador;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", referencedColumnName = "id", nullable = false, updatable = false)
  @NonNull Heladera heladera;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, updatable = false)
  @NonNull MotivoDeDistribucion tipo;

  @Column(name = "parametro")
  Integer parametro;

  public Suscripcion(@NonNull Colaborador colaborador,
                     @NonNull Heladera heladera,
                     @NonNull MotivoDeDistribucion tipo,
                     Integer parametro) {
    this.colaborador = colaborador;
    this.heladera = heladera;
    this.tipo = tipo;
    this.parametro = parametro;
  }

  protected Suscripcion() {
  }
}
