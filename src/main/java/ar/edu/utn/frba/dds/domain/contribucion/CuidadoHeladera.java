package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.Heladera;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

public class CuidadoHeladera extends Contribucion {
        private ZonedDateTime fechaContribucion;

        @Getter
        private Heladera heladera;

        public CuidadoHeladera(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, ZonedDateTime fechaContribucion, Heladera heladera) {
                super(colaborador, fecha);
                this.fechaContribucion = fechaContribucion;
                this.heladera = heladera;
        }

}
