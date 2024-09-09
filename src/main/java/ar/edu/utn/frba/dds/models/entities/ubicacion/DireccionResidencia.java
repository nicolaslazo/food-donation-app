package ar.edu.utn.frba.dds.models.entities.ubicacion;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "direccionResidencia")
public class DireccionResidencia {
  @Id
  @Column(name = "id", columnDefinition = "binary(16)")
  UUID id;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "idUsuario", referencedColumnName = "id", unique = true, nullable = false, updatable = false, columnDefinition = "binary(16)")
  @NonNull Usuario usuario;

  @Column(name = "unidad")
  String unidad;

  @Column(name = "piso")
  String piso;

  @Column(name = "numeroDeCasa", nullable = false)
  @NonNull String numeroDeCasa;

  @Column(name = "calle", nullable = false)
  @NonNull String calle;

  @Column(name = "codigoPostal", nullable = false)
  @NonNull String codigoPostal;

  @Column(name = "barrio", nullable = false)
  @NonNull String barrio;

  @Column(name = "ciudad", nullable = false)
  @NonNull String ciudad;

  @Column(name = "provincia", nullable = false)
  @NonNull String provincia;

  @Column(name = "pais", nullable = false)
  @NonNull String pais;

  public DireccionResidencia(@NonNull Usuario usuario,
                             String unidad,
                             String piso,
                             @NonNull String numeroDeCasa,
                             @NonNull String calle,
                             @NonNull String codigoPostal,
                             @NonNull String barrio,
                             @NonNull String ciudad,
                             @NonNull String provincia,
                             @NonNull String pais) {
    this.usuario = usuario;
    this.unidad = unidad;
    this.piso = piso;
    this.numeroDeCasa = numeroDeCasa;
    this.calle = calle;
    this.codigoPostal = codigoPostal;
    this.barrio = barrio;
    this.ciudad = ciudad;
    this.provincia = provincia;
    this.pais = pais;
  }

  protected DireccionResidencia() {
  }
}
