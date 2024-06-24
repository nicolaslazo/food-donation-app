package ar.edu.utn.frba.dds.models.repositories.incidenteHeladera;

import ar.edu.utn.frba.dds.models.entities.incidente.IncidenteHeladera;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IncidenteRepository implements IIncidenteRepository {
    private List<IncidenteHeladera> incidentesHeladeras;

    public IncidenteRepository() {
        incidentesHeladeras = new ArrayList<IncidenteHeladera>();
    }

    @Override
    public Optional<IncidenteHeladera> getIncidenteHeladera(int id) {
        return incidentesHeladeras
                .stream()
                .filter(inc -> inc.getIdIncidente() == id)
                .findFirst();
    }

    @Override
    public List<IncidenteHeladera> getIncidenteHeladeras() {
        return incidentesHeladeras;
    }

    @Override
    public void insertIncidenteHeladera(IncidenteHeladera incidenteHeladera) {
        incidentesHeladeras.add(incidenteHeladera);
    }

    @Override
    public boolean deleteIncidenteHeladera(int id) {
        Optional<IncidenteHeladera> incidenteHeladera = getIncidenteHeladera(id);
        return incidenteHeladera.map(incidentesHeladeras::remove).orElse(false);
    }

    @Override
    public boolean existsIncidenteHeladera(int id) {
        return getIncidenteHeladera(id).isPresent();
    }

    @Override
    public boolean updateIncidenteHeladera(IncidenteHeladera incidenteHeladera) {
        //TODO
        return false;
    }

    //Lo necesito para generar un ID, despues se puede cambiar si no les gusta
    public int getSize() {
        return incidentesHeladeras.size();
    }
}
