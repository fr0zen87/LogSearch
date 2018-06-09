package com.example.logsearch.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchInfoResultTest {

    private SearchInfoResult searchInfoResult = new SearchInfoResult();
    private List<ResultLogs> resultLogs = new ArrayList<>();

    @Before
    public void setUp() {
        searchInfoResult.setEmptyResultMessage("empty");
        searchInfoResult.setErrorCode(1L);
        searchInfoResult.setErrorMessage("error");
        searchInfoResult.setResultLogs(resultLogs);

        SearchInfoResult result = new SearchInfoResult(1L, "message");
    }

    @Test
    public void getEmptyResultMessage() {
        assertEquals("empty", searchInfoResult.getEmptyResultMessage());
    }

    @Test
    public void getErrorCode() {
        assertEquals(Long.valueOf(1), searchInfoResult.getErrorCode());
    }

    @Test
    public void getErrorMessage() {
        assertEquals("error", searchInfoResult.getErrorMessage());
    }

    @Test
    public void getResultLogs() {
        assertEquals(resultLogs, searchInfoResult.getResultLogs());
    }

    @Test
    public void toStringTest() {
        assertNotNull(searchInfoResult.toString());
    }

    @Test
    public void hashCodeTest() {
        SearchInfoResult result = new SearchInfoResult();
        result.setEmptyResultMessage("empty");
        result.setErrorCode(1L);
        result.setErrorMessage("error");
        result.setResultLogs(resultLogs);

        assertEquals(searchInfoResult.hashCode(), result.hashCode());
    }

    @Test
    public void equalsTest() {
        SearchInfoResult result = new SearchInfoResult();
        result.setEmptyResultMessage("empty");
        result.setErrorCode(1L);
        result.setErrorMessage("error");
        result.setResultLogs(resultLogs);

        assertEquals(searchInfoResult, result);
    }
}