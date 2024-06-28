package ar.edu.utn.frba.dds.models.entities.internalServices;

import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;

public class DonacionViandasService {

  private final DonacionViandasRepository donationRepository;

  // Constructor injection
  public DonacionViandasService(DonacionViandasRepository donationRepository) {
    this.donationRepository = donationRepository;
  }


}
