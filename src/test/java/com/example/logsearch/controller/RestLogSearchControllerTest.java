package com.example.logsearch.controller;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.service.LogService;
import com.example.logsearch.utils.ConfigProperties;
import com.example.logsearch.utils.validators.SearchInfoValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RestLogSearchControllerTest {

    private LogService logService;
    private ConfigProperties configProperties;
    private SearchInfoValidator validator;
    private SearchInfo searchInfo;

    private RestLogSearchController controller;

    @Before
    public void setUp() {
        logService = mock(LogService.class);
        configProperties = mock(ConfigProperties.class);
        validator = mock(SearchInfoValidator.class);
        searchInfo = mock(SearchInfo.class);

        controller = new RestLogSearchController(logService, configProperties, validator);
    }

    @Test
    public void failValidation() {
        when(validator.validate(searchInfo)).thenReturn(new SearchInfoResult(1L, "error"));

        SearchInfoResult result = new SearchInfoResult(1L, "error");
        ResponseEntity<Object> expected = ResponseEntity.badRequest().body(result);
        assertEquals(expected, controller.logSearch(searchInfo));
    }

    @Test
    public void withRealization() {
        when(validator.validate(searchInfo)).thenReturn(null);
        when(searchInfo.isRealization()).thenReturn(true);
        when(configProperties.getFileLink()).thenReturn("link\\link");

        ResponseEntity<Object> expected = ResponseEntity.ok(Collections.singletonMap("link", "link/link"));

        assertEquals(expected, controller.logSearch(searchInfo));
    }

    @Test
    public void withoutRealization() {
        when(validator.validate(searchInfo)).thenReturn(null);
        when(searchInfo.isRealization()).thenReturn(false);

        SearchInfoResult result = new SearchInfoResult();
        when(logService.logSearch(searchInfo)).thenReturn(result);

        assertEquals(ResponseEntity.ok(result), controller.logSearch(searchInfo));
    }
}