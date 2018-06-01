package com.example.logsearch.utils.formatters;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LocalDateTimeAdapterTest {

    private LocalDateTimeAdapter adapter = new LocalDateTimeAdapter();

    @Test
    public void unmarshal() {

        LocalDateTime expected = LocalDateTime.of(2018, 7, 5, 12, 30, 45);

        assertTrue(expected.isEqual(adapter.unmarshal("2018-07-05T12:30:45")));
        assertFalse(expected.isEqual(adapter.unmarshal("2018-07-05T12:30:30")));
    }

    @Test
    public void marshal() {

        String expected = "2018-07-05T12:30:45";
        LocalDateTime localDateTime = LocalDateTime.of(2018, 7, 5, 12, 30, 45);

        assertEquals(expected, adapter.marshal(localDateTime));
        assertNull(adapter.marshal(null));
    }
}