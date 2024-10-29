package ar.edu.utn.frba.dds.controllers.scheduler.jobs;

import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import lombok.Getter;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Set;
import java.util.stream.Collectors;

public class DetectorFraude implements Job {
  @Getter
  static Trigger trigger = TriggerBuilder
      .newTrigger()
      .startNow()
      .withSchedule(SimpleScheduleBuilder.repeatHourlyForever())
      .build();

  @Getter
  static JobDetail jobDetail = JobBuilder.newJob(DetectorFraude.class).withIdentity("jobDetectorFraude").build();

  public void execute(JobExecutionContext ctx) {
    Set<Usuario> fraudulentos = new SolicitudAperturaPorContribucionRepository()
        .getRedistribucionesIncompletasVencidas()
        .map(redistribucion -> redistribucion.getTarjeta().getRecipiente())
        .collect(Collectors.toSet());

    for (Usuario usuario : fraudulentos) {
      usuario.setActivo(false);
      new UsuariosRepository().update(usuario);

      new ContactosRepository()
          .get(usuario)
          .forEach(contacto ->
          {
            try {
              contacto.enviarMensaje(
                  "Su cuenta ha sido desactivada por sospechas de fraude. " +
                      "Puede contactarse con un administrador de la plataforma para tener más detalles.");
            } catch (MensajeAContactoException e) {
              throw new RuntimeException(e);
            }
          });
    }
  }
}
