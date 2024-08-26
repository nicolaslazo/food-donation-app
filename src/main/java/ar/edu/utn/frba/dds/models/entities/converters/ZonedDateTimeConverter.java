package ar.edu.utn.frba.dds.models.entities.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, OffsetDateTime> {
    @Override
    public OffsetDateTime convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toOffsetDateTime() : null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? offsetDateTime.atZoneSameInstant(ZoneOffset.UTC) : null;
    }
}
