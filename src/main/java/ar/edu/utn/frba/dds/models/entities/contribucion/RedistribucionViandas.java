package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;

public class RedistribucionViandas extends MovimientoViandas {
  // Algunos de estos se mantienen nulificables para el cargador masivo CSV

  @Getter
  final Heladera origen;

  @Enumerated(EnumType.STRING)
  @Column(name = "motivo")
  final MotivoDeDistribucion motivo;

  @Column(name = "fechaIniciada")
  ZonedDateTime fechaIniciada = null;

  public RedistribucionViandas(Colaborador colaborador,
                               Collection<Vianda> viandas,
                               Heladera destino,
                               Heladera origen,
                               MotivoDeDistribucion motivo) {
    super(colaborador, viandas, destino);
    this.origen = origen;
    this.motivo = motivo;
  }

  @Override
  public String toString() {
    return "RedistribucionViandas{" +
        "origen=" + origen +
        ", destino=" + destino +
        ", motivo=" + motivo +
        ", colaborador=" + colaborador +
        ", fechaIniciada=" + fechaIniciada +
        ", fechaRealizada=" + fechaRealizada +
        '}';
  }

  public void setFechaIniciada(ZonedDateTime timestamp) throws Exception {
    if (fechaIniciada != null) throw new Exception("Esta contribución ya fue iniciada");

    viandas
        .stream()
        .filter(Objects::nonNull)  // Caso especial por las contribuciones legacy
        .forEach(vianda -> vianda.setHeladera(null));

    fechaIniciada = timestamp;
  }
}
