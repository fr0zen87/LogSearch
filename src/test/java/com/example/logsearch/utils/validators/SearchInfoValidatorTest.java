package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.logsearch.entities.CorrectionCheckResult.*;
import static com.example.logsearch.entities.CorrectionCheckResult.ERROR18;
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
        searchInfo.setLocation(System.getProperty("user.dir"));
        searchInfo.setRealization(false);
        searchInfo.setFileExtension(FileExtension.DOC);
        intervals = new ArrayList<>();
        intervals.add(new SignificantDateInterval());
        searchInfo.setDateIntervals(intervals);

        validator = new SearchInfoValidator();
    }

    @Test
    public void check3701Error() {
        searchInfo.setRealization(false);
        searchInfo.setFileExtension(FileExtension.DOC);
        assertNull(validator.validate(searchInfo));

        searchInfo.setFileExtension(null);
        assertNull(validator.validate(searchInfo));

        searchInfo.setRealization(true);
        searchInfo.setFileExtension(FileExtension.DOC);
        assertNull(validator.validate(searchInfo));

        searchInfo.setFileExtension(null);
        assertNotNull(validator.validate(searchInfo));
    }

    @Test
    public void nullLocationTest() {
        searchInfo.setLocation(null);
        assertNull(validator.validate(searchInfo));
    }

    @Test
    public void check44Error() {
        searchInfo.setLocation("asd");
        expected = new SearchInfoResult(ERROR44.getErrorCode(), ERROR44.getErrorMessage());
        assertEquals(expected, validator.validate(searchInfo));
    }

    @Test
    public void check37ErrorTest() {
        searchInfo.setRegularExpression(null);
        expected = new SearchInfoResult(ERROR37.getErrorCode(), ERROR37.getErrorMessage());
        assertEquals(expected, validator.validate(searchInfo));

        searchInfo.setDateIntervals(null);
        expected = new SearchInfoResult(ERROR37.getErrorCode(), ERROR37.getErrorMessage());
        assertEquals(expected, validator.validate(searchInfo));

        searchInfo.setRegularExpression("");
        expected = new SearchInfoResult(ERROR37.getErrorCode(), ERROR37.getErrorMessage());
        assertEquals(expected, validator.validate(searchInfo));
    }

    @Test
    public void check19ErrorTest() {
    }

    @Test
    public void check18ErrorTest() {
        LocalDateTime from = LocalDateTime.MAX;
        intervals.get(0).setDateFrom(from);
        expected = new SearchInfoResult(ERROR18.getErrorCode(), ERROR18.getErrorMessage());
        assertEquals(expected, validator.validate(searchInfo));
    }

    @Test
    public void check1ErrorTest() {
        LocalDateTime from = LocalDateTime.of(2018, 2,1,0,0);
        LocalDateTime to = LocalDateTime.of(2018, 1,1,0,0);
        intervals.get(0).setDateFrom(from);
        intervals.get(0).setDateTo(to);
        expected = new SearchInfoResult(ERROR1.getErrorCode(), ERROR1.getErrorMessage());
        assertEquals(expected, validator.validate(searchInfo));
    }

    @Test
    public void fillIntervalsTest() {
        intervals.add(new SignificantDateInterval());
        assertNull(intervals.get(0).getDateFrom());
        assertNull(intervals.get(0).getDateTo());

        SearchInfoValidator.fillIntervals(intervals);
        assertEquals(LocalDateTime.MIN, intervals.get(0).getDateFrom());
        assertEquals(LocalDateTime.MAX, intervals.get(0).getDateTo());

        searchInfo.setDateIntervals(new ArrayList<>());
        assertTrue(searchInfo.getDateIntervals().isEmpty());
    }
}