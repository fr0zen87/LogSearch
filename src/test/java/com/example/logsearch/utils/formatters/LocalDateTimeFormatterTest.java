package com.example.logsearch.utils.formatters;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocalDateTimeFormatterTest {

    private LocalDateTimeFormatter formatter = new LocalDateTimeFormatter();

    @Test
    public void parse() {

        LocalDateTime expected = LocalDateTime.of(2018, 7, 5, 12, 30);

        assertEquals(expected, formatter.parse("2018-07-05T12:30", Locale.getDefault()));
    }

    @Test
    public void print() {

        String expected = "2018-07-05T12:30";
        LocalDateTime actual = LocalDateTime.of(2018, 7, 5, 12, 30);

        assertEquals(expected, formatter.print(actual, Locale.getDefault()));
    }
}