package ar.edu.utn.frba.dds.migrations;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FechaDeContribucionConverter extends AbstractBeanField<ZonedDateTime, String> {
  @Override
  protected ZonedDateTime convert(String valor) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(valor, formatter).atStartOfDay(ZoneId.of("America/Argentina/Buenos_Aires"));
  }
}