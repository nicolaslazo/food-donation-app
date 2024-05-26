package ar.edu.utn.frba.dds.domain.utils;

import ar.edu.utn.frba.dds.domain.contribucion.Contribucion;

import java.util.List;
import java.util.stream.Collectors;

//Filtra y cuenta contribuciones en especifico, no general.
public class FiltradorContribuciones {
    private static FiltradorContribuciones instancia;

    private FiltradorContribuciones() {}

    public static FiltradorContribuciones getInstance() {
        if (instancia == null) {
            instancia = new FiltradorContribuciones();
        }
        return instancia;
    }

    public <T extends Contribucion> List<T> filtrarContribucionesPorTipo(Class<T> tipo,
                                                                         List<Contribucion> contribuciones) {
        return contribuciones.stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .collect(Collectors.toList());
    }
}
