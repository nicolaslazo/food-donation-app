package ar.edu.utn.frba.dds.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

import java.time.LocalDate;

public record Contribucion(TipoContribucion tipo, Colaborador colaborador, LocalDate fecha, int cantidad) {}
