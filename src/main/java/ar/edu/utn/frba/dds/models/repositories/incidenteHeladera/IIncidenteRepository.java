package ar.edu.utn.frba.dds.models.repositories.incidenteHeladera;

import ar.edu.utn.frba.dds.models.entities.incidente.IncidenteHeladera;

import java.util.List;
import java.util.Optional;

public interface IIncidenteRepository {

    Optional<IncidenteHeladera> getIncidenteHeladera(int id);

    List<IncidenteHeladera> getIncidenteHeladeras();

    void insertIncidenteHeladera(IncidenteHeladera incidenteHeladera);

    boolean deleteIncidenteHeladera(int id);

    boolean existsIncidenteHeladera(int id);

    boolean updateIncidenteHeladera(IncidenteHeladera incidenteHeladera);

    int getSize();

}
