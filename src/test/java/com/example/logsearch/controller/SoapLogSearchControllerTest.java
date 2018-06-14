package com.example.logsearch.controller;

import com.example.logsearch.entities.ObjectResponse;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.service.LogService;
import com.example.logsearch.utils.ConfigProperties;
import com.example.logsearch.utils.validators.SearchInfoValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoapLogSearchControllerTest {

    private LogService logService;
    private ConfigProperties configProperties;
    private SearchInfoValidator validator;
    private SearchInfo searchInfo;

    private SoapLogSearchController controller;

    @Before
    public void setUp() {
        logService = mock(LogService.class);
        configProperties = mock(ConfigProperties.class);
        validator = mock(SearchInfoValidator.class);
        searchInfo = mock(SearchInfo.class);

        controller = new SoapLogSearchController(logService, configProperties, validator);
    }

    @Test
    public void failValidation() {
        when(validator.validate(searchInfo)).thenReturn(new SearchInfoResult(1L, "error"));

        SearchInfoResult result = new SearchInfoResult(1L, "error");
        ObjectResponse expected = new ObjectResponse(result);
        assertEquals(expected.getSearchInfoResult(),
                controller.logSearch(searchInfo).getSearchInfoResult());
    }

    @Test
    public void withRealization() {
        when(validator.validate(searchInfo)).thenReturn(null);
        when(searchInfo.isRealization()).thenReturn(true);
        when(configProperties.getFileLink()).thenReturn("link\\link");

        ObjectResponse expected = new ObjectResponse("link/link");

        assertEquals(expected.getLink(), controller.logSearch(searchInfo).getLink());
    }

    @Test
    public void withoutRealization() {
        when(validator.validate(searchInfo)).thenReturn(null);
        when(searchInfo.isRealization()).thenReturn(false);

        SearchInfoResult result = new SearchInfoResult();
        when(logService.logSearch(searchInfo)).thenReturn(result);

        assertEquals(new ObjectResponse(result).getSearchInfoResult(),
                controller.logSearch(searchInfo).getSearchInfoResult());
    }
}