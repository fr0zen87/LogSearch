package com.example.logsearch.entities;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class SignificantDateIntervalTest {

    private SignificantDateInterval significantDateInterval = new SignificantDateInterval();

    @Before
    public void setUp() {
        significantDateInterval.setDateFrom(LocalDateTime.MIN);
        significantDateInterval.setDateTo(LocalDateTime.MAX);

        SignificantDateInterval interval = new SignificantDateInterval(LocalDateTime.MIN, LocalDateTime.MAX);
    }

    @Test
    public void getDateFrom() {
        assertEquals(LocalDateTime.MIN, significantDateInterval.getDateFrom());
    }

    @Test
    public void getDateTo() {
        assertEquals(LocalDateTime.MAX, significantDateInterval.getDateTo());
    }

    @Test
    public void toStringTest() {
        assertNotNull(significantDateInterval.toString());
    }
}