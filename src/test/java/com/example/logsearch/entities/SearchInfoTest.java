package com.example.logsearch.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchInfoTest {

    private SearchInfo searchInfo = new SearchInfo();
    private List<SignificantDateInterval> intervals = new ArrayList<>();

    @Before
    public void setUp() {
        searchInfo.setRegularExpression("regexp");
        searchInfo.setLocation("domains");
        searchInfo.setRealization(true);
        searchInfo.setFileExtension(FileExtension.DOC);
        searchInfo.setDateIntervals(intervals);
    }

    @Test
    public void getRegularExpression() {
        assertEquals("regexp", searchInfo.getRegularExpression());
    }

    @Test
    public void getDateIntervals() {
        assertEquals(intervals, searchInfo.getDateIntervals());
    }

    @Test
    public void getLocation() {
        assertEquals("domains", searchInfo.getLocation());
    }

    @Test
    public void isRealization() {
        assertTrue(searchInfo.isRealization());
    }

    @Test
    public void getFileExtension() {
        assertEquals(FileExtension.DOC, searchInfo.getFileExtension());
    }

    @Test
    public void toStringTest() {
        assertNotNull(searchInfo.toString());
    }
}