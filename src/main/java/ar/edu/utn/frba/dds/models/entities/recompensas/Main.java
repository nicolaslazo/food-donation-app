package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    // TODO: convertir en un test
    // Crear un colaborador
    TipoDocumento documento = TipoDocumento.DNI;
    Colaborador colaborador = new Colaborador(
        new Documento(documento, 12345678), "Juan", "Perez",
        new ContactoEmail("juan@example.com")
    );

    // Contribuciones de dinero
    Dinero dinero1 = new Dinero(colaborador, ZonedDateTime.now(), 100, 30);
    Dinero dinero2 = new Dinero(colaborador, ZonedDateTime.now(), 200, 10);
    Dinero dinero3 = new Dinero(colaborador, ZonedDateTime.now(), 200, 12);

    // Contribuciones de donación de viandas
    List<Vianda> viandas = new ArrayList<>();
    viandas.add(new Vianda());
    viandas.add(new Vianda());
    DonacionViandas donacionViandas = new DonacionViandas(colaborador, ZonedDateTime.now(), viandas);

    // Contribuciones de entrega de tarjetas

    PersonaVulnerable personaVulnerable = new PersonaVulnerable(
        new Documento(TipoDocumento.DNI, 12345678), // Documento
        colaborador, // Colaborador reclutador
        "Nombre", // Nombre
        "Apellido", // Apellido
        ZonedDateTime.now().minusYears(30), // Fecha de nacimiento (por ejemplo, hace 30 años)
        ZonedDateTime.now(), // Fecha registrada (por ejemplo, ahora)
        null, // Ubicación
        2 // Menores a cargo
    );

    List<TarjetaAlimentaria> tarjetas = new ArrayList<>();
    tarjetas.add(new TarjetaAlimentaria("ABC12345678", personaVulnerable, colaborador));
    tarjetas.add(new TarjetaAlimentaria("DEF87654321", personaVulnerable, colaborador));
    EntregaTarjetas entregaTarjetas = new EntregaTarjetas(colaborador, ZonedDateTime.now(), tarjetas);

    // Contribuciones de redistribución de viandas
    List<Vianda> viandasRedistribucion = new ArrayList<>();
    viandasRedistribucion.add(new Vianda());
    viandasRedistribucion.add(new Vianda());
    Heladera origen = new Heladera(0, 10, 5);
    Heladera destino = new Heladera(0, 10, 5);
    RedistribucionViandas redistribucionViandas = new RedistribucionViandas(colaborador, ZonedDateTime.now(), MotivoDeDistribucion.FALLA_HELADERA_ORIGEN, origen, destino, viandasRedistribucion);

    System.out.println("Total: " + CalculadoraDePuntos.calcular(colaborador));
  }
}




