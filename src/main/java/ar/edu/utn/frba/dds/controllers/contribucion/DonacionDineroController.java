package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DineroRepository;
import io.javalin.http.Context;

public class DonacionDineroController {
  public void index(Context context) {
    context.render("contribuciones/donacion_dinero/donacion_dinero.hbs");
  }

  public void create(Context context) {
    Colaborador colaborador = new ColaboradorRepository().findById(context.sessionAttribute("user_id")).get();
    Integer frecuenciaEnDias = Boolean.parseBoolean(context.formParam("donacion-periodica")) ?
        Integer.parseInt(context.formParam("frecuencia"))
        : null;
    Dinero donacion = new Dinero(colaborador,
        Double.parseDouble(context.formParam("numeroTarjeta")),
        frecuenciaEnDias);
    new DineroRepository().insert(donacion);

    context.redirect("/quiero-ayudar");
  }
}
