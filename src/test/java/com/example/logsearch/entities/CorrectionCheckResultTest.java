package com.example.logsearch.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class CorrectionCheckResultTest {

    @Test
    public void getErrorCode() {
        long actual = CorrectionCheckResult.ERROR1.getErrorCode();

        assertEquals(1, actual);
        assertNotEquals(5, actual);
    }

    @Test
    public void getErrorMessage() {
        String expected = "DateFrom exceeds DateTo";
        String actual = CorrectionCheckResult.ERROR1.getErrorMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void getErrorComment() {
        String expected = "Нижняя граница превосходит верхнюю";
        String actual = CorrectionCheckResult.ERROR1.getErrorComment();

        assertEquals(expected, actual);
    }
}