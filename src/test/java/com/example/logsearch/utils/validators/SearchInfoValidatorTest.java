package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.logsearch.entities.CorrectionCheckResult.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SearchInfoValidatorTest {

    private SearchInfo searchInfo;
    private List<SignificantDateInterval> intervals;
    private SearchInfoResult expected;

    private SearchInfoValidator validator;

    @Before
    public void setUp() {
        searchInfo = mock(SearchInfo.class);
        intervals = new ArrayList<>();
        validator = new SearchInfoValidator();
    }

    @Test
    public void realizationWithoutExtensionTest() {

        expected = new SearchInfoResult(ERROR3701.getErrorCode(), ERROR3701.getErrorMessage());

        when(searchInfo.isRealization()).thenReturn(true);
        when(searchInfo.getFileExtension()).thenReturn(null);

        assertTrue(searchInfo.isRealization());
        assertNull(searchInfo.getFileExtension());
        assertEquals(expected, validator.validate(searchInfo));
    }

    @Test
    public void noRealizationTest() {

        when(searchInfo.isRealization()).thenReturn(false);
        when(searchInfo.getFileExtension()).thenReturn(FileExtension.DOC);

        assertFalse(searchInfo.isRealization());
        assertEquals(FileExtension.DOC, searchInfo.getFileExtension());
    }

    @Test
    public void nullLocationTest() {

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