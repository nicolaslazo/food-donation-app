package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.inputs.tecnicos.DataTecnico;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.entities.utils.permisos.VerificadorDePermisos;

public class TecnicoController {
  private VerificadorDePermisos verificador;

  public void darDeAltaTecnico(DataTecnico tecnico, Usuario usuario) {
    verificador.verificarSiUsuarioPuede("DAR_DE_ALTA_TECNICO", usuario);
  }
}
