package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;

import lombok.Getter;


import java.util.UUID;


@Getter
public class TecnicoRepository extends HibernateEntityManager<Tecnico, UUID> {

}
