package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.example.logsearch.entities.CorrectionCheckResult.*;
import static org.junit.Assert.*;

public class SearchInfoValidatorTest {

    private SearchInfoValidator validator = new SearchInfoValidator();

    private SearchInfo searchInfo = new SearchInfo();

    private SearchInfoResult expected;

    @Before
    public void init() {
        searchInfo.setRegularExpression("");
        searchInfo.setFileExtension(FileExtension.DOC);
        searchInfo.setLocation("");
        searchInfo.setRealization(false);
        List<SignificantDateInterval> intervals = new ArrayList<>();
        intervals.add(new SignificantDateInterval());
        searchInfo.setDateIntervals(intervals);
    }

    @Test
    public void allPassedTest() {
        assertNull(validator.validate(searchInfo));
    }

    @Test
    public void regexTest() {
        searchInfo.setRegularExpression(null);

        expected = new SearchInfoResult(ERROR37.getErrorCode(), ERROR37.getErrorMessage());
        SearchInfoResult actual = validator.validate(searchInfo);
        assertEquals(expected.getErrorCode(), actual.getErrorCode());
        assertEquals(expected.getErrorMessage(), actual.getErrorMessage());
    }

    @Test
    public void fileExtensionWithoutRealizationTest() {
        searchInfo.setRealization(false);
        assertNull(validator.validate(searchInfo));

        searchInfo.setFileExtension(null);
        assertNull(validator.validate(searchInfo));
    }

    @Test
    public void fileExtensionWithRealizationTest() {
        searchInfo.setRealization(true);

        searchInfo.setFileExtension(FileExtension.DOC);
        assertNull(validator.validate(searchInfo));

        searchInfo.setFileExtension(null);
        expected = new SearchInfoResult(ERROR3701.getErrorCode(), ERROR3701.getErrorMessage());
        SearchInfoResult actual = validator.validate(searchInfo);
        assertEquals(expected.getErrorCode(), actual.getErrorCode());
        assertEquals(expected.getErrorMessage(), actual.getErrorMessage());
    }

    @Test
    public void locationTest() {
        String existsPath = Paths.get(System.getProperty("user.dir")).getParent().getFileName().toString();
        String notExistsPath = "notExists";

        searchInfo.setLocation(existsPath);
        assertNull(validator.validate(searchInfo));

        searchInfo.setLocation("\\" + existsPath);
        assertNull(validator.validate(searchInfo));

        searchInfo.setLocation("/" + existsPath);
        assertNull(validator.validate(searchInfo));

        searchInfo.setLocation(notExistsPath);
        expected = new SearchInfoResult(ERROR44.getErrorCode(), ERROR44.getErrorMessage());
        SearchInfoResult actual = validator.validate(searchInfo);
        assertEquals(expected.getErrorCode(), actual.getErrorCode());
        assertEquals(expected.getErrorMessage(), actual.getErrorMessage());

        searchInfo.setLocation(null);
        assertNull(validator.validate(searchInfo));
    }
}