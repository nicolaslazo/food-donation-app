package ar.edu.utn.frba.dds.models.entities.sensores.configuraciones;

import ar.edu.utn.frba.dds.models.entities.sensores.AccionadorHeladera;
import ar.edu.utn.frba.dds.models.entities.sensores.ReceptorTemperatura;
import ar.edu.utn.frba.dds.models.entities.sensores.comandos.ComandoHeladera;
import ar.edu.utn.frba.dds.models.entities.sensores.comandos.PonerInactivaHeladera;
import ar.edu.utn.frba.dds.models.entities.sensores.comandos.RegistrarTiempoTemperatura;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionSensores {
    /*>> Bootstrap {
		receptorFugaDeGas = new ReceptorFugaDeGas();
		accionadorParaFugaDeGas = new Accionador();
		accionadorParaFugaDeGas.agregarComando(new MostrarAvisoPorPantalla("Hay fuga de gas"));

		reeptorFugaDeGas.setAccionador(accionadorParaFugaDeGas);

		receptorTensionDeLuz = new ReceptorTensionLuz();
		accionadorParaTensionBajaDeLuz = new Accionador();
		accionadorParaTensionBajaDeLuz.agregarComando(new MostrarAvisoPorPantalla("Hay tensión baja de luz"));

		receptorTensionDeLuz.setAccionador(accionadorParaTensionBajaDeLuz);
		....
	}*/
    /*
    List<ComandoHeladera> comandosParaTemperatura = new ArrayList<>();


    AccionadorHeladera accionadorParaTemperatura = AccionadorHeladera.builder()
            .comandosHeladeras(comandosParaTemperatura)
            .build();


    ReceptorTemperatura receptorTemperatura = ReceptorTemperatura.builder()
            .temperaturaMinima(-5.0)
            .temperaturaMaxima(5.0)
            .accionador(accionadorParaTemperatura)
            .build();


    */
}
