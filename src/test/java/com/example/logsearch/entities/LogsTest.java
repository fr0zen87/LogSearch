package com.example.logsearch.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogsTest {

    private Logs logs = new Logs();
    private SearchInfo searchInfo = new SearchInfo();
    private SearchInfoResult searchInfoResult = new SearchInfoResult();

    @Before
    public void setUp() {
        logs.setCreator("creator");
        logs.setSearchInfo(searchInfo);
        logs.setSearchInfoResult(searchInfoResult);
        logs.setApplication("app");

        Logs fullLogs = new Logs("creator", searchInfo, searchInfoResult, "app");
    }

    @Test
    public void getCreator() {
        assertEquals("creator", logs.getCreator());
    }

    @Test
    public void getSearchInfo() {
        assertEquals(searchInfo, logs.getSearchInfo());
    }

    @Test
    public void getSearchInfoResult() {
        assertEquals(searchInfoResult, logs.getSearchInfoResult());
    }

    @Test
    public void getApplication() {
        assertEquals("app", logs.getApplication());
    }
}