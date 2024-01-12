package dev.kwolszczak.peopledb.web.formatter.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {

    private DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd MMMM, yyyy");

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text,DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return dateTimeFormatter.format(object);
    }
}
