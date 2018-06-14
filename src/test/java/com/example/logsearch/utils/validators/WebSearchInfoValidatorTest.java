package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;
import org.springframework.web.bind.EscapedErrors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.logsearch.entities.CorrectionCheckResult.*;
import static com.example.logsearch.entities.CorrectionCheckResult.ERROR1;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WebSearchInfoValidatorTest {

    private SearchInfo searchInfo;
    private List<SignificantDateInterval> intervals;
    private Errors errors;

    private WebSearchInfoValidator validator;

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

        errors = mock(Errors.class);

        validator = new WebSearchInfoValidator();
    }

    @Test
    public void supportsTest() {
        assertTrue(validator.supports(SearchInfo.class));
    }

    @Test
    public void allCorrectTest() {
        validator.validate(searchInfo, errors);
    }

    @Test
    public void check3701Error() {
        searchInfo.setFileExtension(null);
        validator.validate(searchInfo, errors);

        searchInfo.setRealization(true);
        searchInfo.setFileExtension(FileExtension.DOC);
        validator.validate(searchInfo, errors);

        searchInfo.setRealization(true);
        searchInfo.setFileExtension(null);
        doNothing().when(errors).rejectValue("fileExtension", ERROR3701.getErrorMessage());
        validator.validate(searchInfo, errors);
        verify(errors).rejectValue("fileExtension", ERROR3701.getErrorMessage());
    }

    @Test
    public void nullLocationTest() {
        searchInfo.setLocation(null);
        validator.validate(searchInfo, errors);
    }

    @Test
    public void check44Error() {
        searchInfo.setLocation("asd");
        doNothing().when(errors).rejectValue("location", ERROR44.getErrorMessage());
        validator.validate(searchInfo, errors);
        verify(errors).rejectValue("location", ERROR44.getErrorMessage());
    }

    @Test
    public void check37ErrorTest() {
        searchInfo.setRegularExpression(null);
        doNothing().when(errors).rejectValue("regularExpression", ERROR37.getErrorMessage());
        validator.validate(searchInfo, errors);
        verify(errors).rejectValue("regularExpression", ERROR37.getErrorMessage());
    }

    @Test
    public void check19ErrorTest() {
    }

    @Test
    public void check18ErrorTest() {
        LocalDateTime from = LocalDateTime.MAX;
        intervals.get(0).setDateFrom(from);
        doNothing().when(errors).rejectValue("exceedsPresentTime", ERROR18.getErrorMessage());
        validator.validate(searchInfo, errors);
        verify(errors).rejectValue("exceedsPresentTime", ERROR18.getErrorMessage());
    }

    @Test
    public void check1ErrorTest() {
        LocalDateTime from = LocalDateTime.of(2018, 2,1,0,0);
        LocalDateTime to = LocalDateTime.of(2018, 1,1,0,0);
        intervals.get(0).setDateFrom(from);
        intervals.get(0).setDateTo(to);
        doNothing().when(errors).rejectValue("exceedsDateTo", ERROR1.getErrorMessage());
        validator.validate(searchInfo, errors);
        verify(errors).rejectValue("exceedsDateTo", ERROR1.getErrorMessage());
    }
}