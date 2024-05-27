package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.DataTecnico;
import ar.edu.utn.frba.dds.models.entities.Permiso;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.models.repositories.PermisosRepository;

public class TecnicoController {
    public void darDeAltaTecnico(DataTecnico tenico, Usuario usuario) {
        Permiso permiso = PermisosRepository.buscarPermisoPorNombre("DAR_DE_ALTA_TECNICO");
    }
}
