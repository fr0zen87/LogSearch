package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SearchInfoValidatorTest {

    private SearchInfo searchInfo;
    private List<SignificantDateInterval> intervals;
    private SearchInfoResult expected;

    private SearchInfoValidator validator;

    @Before
    public void setUp() {
        searchInfo = new SearchInfo();
        searchInfo.setRegularExpression("");
        searchInfo.setLocation("");
        searchInfo.setRealization(false);
        searchInfo.setFileExtension(FileExtension.DOC);
        intervals = new ArrayList<>();
        searchInfo.setDateIntervals(intervals);

        validator = new SearchInfoValidator();
    }

    @Test
    public void validate() {

    }

    @Test
    public void fillIntervalsTest() {
        assertTrue(intervals.isEmpty());

        intervals.add(new SignificantDateInterval());
        assertNull(intervals.get(0).getDateFrom());
        assertNull(intervals.get(0).getDateTo());

        SearchInfoValidator.fillIntervals(intervals);
        assertEquals(LocalDateTime.MIN, intervals.get(0).getDateFrom());
        assertEquals(LocalDateTime.MAX, intervals.get(0).getDateTo());
    }
}