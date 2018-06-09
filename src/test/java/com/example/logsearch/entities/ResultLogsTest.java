package com.example.logsearch.entities;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ResultLogsTest {

    private ResultLogs resultLogs = new ResultLogs();

    @Before
    public void setUp() {
        resultLogs.setTimeMoment(LocalDateTime.MAX);
        resultLogs.setFileName("file name");
        resultLogs.setContent("content");
    }

    @Test
    public void getTimeMoment() {
        assertEquals(LocalDateTime.MAX, resultLogs.getTimeMoment());
    }

    @Test
    public void getFileName() {
        assertEquals("file name", resultLogs.getFileName());
    }

    @Test
    public void getContent() {
        assertEquals("content", resultLogs.getContent());
    }

    @Test
    public void toStringTest() {
        assertNotNull(resultLogs.toString());
    }
}