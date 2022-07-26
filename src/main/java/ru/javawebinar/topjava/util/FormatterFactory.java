package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;


public class FormatterFactory implements AnnotationFormatterFactory<DateTimeFormat> {
    private static final Formatter<LocalDate> dateFormatter = new DateFormatter();
    private static final Formatter<LocalTime> timeFormatter = new TimeFormatter();

    @Override
    public Set<Class<?>> getFieldTypes() {
        return Set.of(LocalDate.class, LocalTime.class);
    }

    @Override
    public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatter(annotation, fieldType);
    }

    private Formatter<?> configureFormatter(DateTimeFormat annotation, Class<?> fieldType) {
        if (fieldType.equals(LocalDate.class)) {
            return dateFormatter;
        }
        if (fieldType.equals(LocalTime.class)) {
            return timeFormatter;
        }
        return null;
    }

    private static class DateFormatter implements Formatter<LocalDate> {
        @Override
        public String print(LocalDate object, Locale locale) {
            return object.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public LocalDate parse(String text, Locale locale) {
            return LocalDate.parse(text);
        }
    }

    private static class TimeFormatter implements Formatter<LocalTime> {
        @Override
        public String print(LocalTime object, Locale locale) {
            return object.format(DateTimeFormatter.ISO_LOCAL_TIME);
        }

        @Override
        public LocalTime parse(String text, Locale locale) {
            return LocalTime.parse(text);
        }
    }
}
