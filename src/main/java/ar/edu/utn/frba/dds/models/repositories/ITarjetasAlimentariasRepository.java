package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;

import java.util.Optional;

public interface ITarjetasAlimentariasRepository {
    public void insert(TarjetaAlimentaria tarjetaAlimentaria);
    public Optional<TarjetaAlimentaria> getPorIdentificador(String id);

}
