package com.example.logsearch.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigPropertiesTest {

    private ConfigProperties properties;

    @Before
    public void setUp() {
        properties = new ConfigProperties();
        properties.setPath("path");
        properties.setFileLink("link");
        properties.setDeletionInterval(5);
        properties.setFileExistTime(2);
        properties.setThreadsNumber(32);
    }

    @Test
    public void getPath() {
        assertEquals("path", properties.getPath());
    }

    @Test
    public void getFileLink() {
        assertEquals("link", properties.getFileLink());
    }

    @Test
    public void getDeletionInterval() {
        assertEquals(5, properties.getDeletionInterval());
    }

    @Test
    public void getFileExistTime() {
        assertEquals(2, properties.getFileExistTime());
    }

    @Test
    public void getThreadsNumber() {
        assertEquals(32, properties.getThreadsNumber());
    }
}