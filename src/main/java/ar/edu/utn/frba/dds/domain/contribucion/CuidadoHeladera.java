package ar.edu.utn.frba.dds.domain.contribucion;

import ar.edu.utn.frba.dds.domain.Heladera;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.List;
public class CuidadoHeladera extends Contribucion {
        private ZonedDateTime fechaContribucion;

        //TODO
        //SE HACE CARGO DE VARIAS HELADERAS A LA VEZ? O SOLO UNA POR CONTRIBUCION?
        @Getter
        private List<Heladera> heladeras;

        public CuidadoHeladera(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, ZonedDateTime fechaContribucion, List<Heladera> heladeras) {
                super(colaborador, fecha);
                this.fechaContribucion = fechaContribucion;
                this.heladeras = heladeras;
        }

}
