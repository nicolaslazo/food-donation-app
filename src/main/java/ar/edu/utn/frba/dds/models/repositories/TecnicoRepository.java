package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TecnicoRepository implements ITecnicoRepository {
    private final List<Tecnico> tecnicos;

    public TecnicoRepository() { tecnicos = new ArrayList<Tecnico>(); }

    public Optional<Tecnico> getTecnico(String cuil) {
        return tecnicos
                .stream()
                .filter(tecnico -> tecnico.getCuil().equals(cuil))
                .findFirst();
    }

    public List<Tecnico> getTecnicos() {
        return tecnicos;
    }

    public void insertTecnico(Tecnico tecnico) {
        tecnicos.add(tecnico);
    }

    public boolean deleteTecnico(String cuil) {
        Optional<Tecnico> tecnicoOptional = getTecnico(cuil);
        return tecnicoOptional.map(tecnicos::remove).orElse(false);
    }

    public boolean updateTecnico(Tecnico tecnico) {
        //TODO
        return true;
    }


}
