package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;

import java.util.List;
import java.util.Optional;

public interface ITecnicoRepository {
    public Optional<Tecnico> getTecnico(String cuil);
    public List<Tecnico> getTecnicos();
    public void insertTecnico(Tecnico tecnico);
    public boolean deleteTecnico(String cuil);
    public boolean updateTecnico(Tecnico tecnico);
}
