package ar.edu.utn.frba.dds.models.repositories.incidenteHeladera;

import ar.edu.utn.frba.dds.models.entities.incidente.Incidente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IncidenteRepository implements IIncidenteRepository {
    IncidenteRepository instancia = null;
    private List<Incidente> incidentesHeladeras;

    public IncidenteRepository() {
        incidentesHeladeras = new ArrayList<Incidente>();
    }

    @Override
    public Optional<Incidente> getIncidenteHeladera(int id) {
        return incidentesHeladeras
                .stream()
                .filter(inc -> inc.getIdIncidente() == id)
                .findFirst();
    }

    public IncidenteRepository getInstancia() {
        if (instancia == null) {
            instancia = new IncidenteRepository();
        }
        return instancia;
    }

    @Override
    public List<Incidente> getIncidenteHeladeras() {
        return incidentesHeladeras;
    }

    @Override
    public void insertIncidenteHeladera(Incidente incidente) {
        incidente.setIdIncidente(incidentesHeladeras.size()+1);
        incidentesHeladeras.add(incidente);
    }

    @Override
    public boolean deleteIncidenteHeladera(int id) {
        Optional<Incidente> incidenteHeladera = getIncidenteHeladera(id);
        return incidenteHeladera.map(incidentesHeladeras::remove).orElse(false);
    }

    @Override
    public boolean existsIncidenteHeladera(int id) {
        return getIncidenteHeladera(id).isPresent();
    }

    @Override
    public boolean updateIncidenteHeladera(Incidente incidenteHeladera) {
        //TODO
        return false;
    }

}
