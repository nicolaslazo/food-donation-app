package ar.edu.utn.frba.dds.models.repositories;


import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarjetasAlimentariasRepository implements ITarjetasAlimentariasRepository {
    private final List<TarjetaAlimentaria> tarjetasAlimentarias;

    public TarjetasAlimentariasRepository() { tarjetasAlimentarias = new ArrayList<>(); }

    public void insert(TarjetaAlimentaria tarjetaAlimentaria) {
        tarjetasAlimentarias.add(tarjetaAlimentaria);
    }

    public Optional<TarjetaAlimentaria> getPorIdentificador(String id) {
        return tarjetasAlimentarias
                .stream()
                .filter(ta -> ta.identificador().equals(id))
                .findFirst();
    }
}
