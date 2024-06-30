package ar.edu.utn.frba.dds.models.repositories.incidenteHeladera;
import ar.edu.utn.frba.dds.models.entities.incidente.Incidente;

import java.util.List;
import java.util.Optional;

public interface IIncidenteRepository {

    Optional<Incidente> getIncidenteHeladera(int id);

    List<Incidente> getIncidenteHeladeras();

    void insertIncidenteHeladera(Incidente incidenteHeladera);

    boolean deleteIncidenteHeladera(int id);

    boolean existsIncidenteHeladera(int id);

    boolean updateIncidenteHeladera(Incidente incidenteHeladera);


}
