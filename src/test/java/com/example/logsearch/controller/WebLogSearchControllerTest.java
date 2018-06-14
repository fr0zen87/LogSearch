package com.example.logsearch.controller;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.service.LogService;
import com.example.logsearch.utils.ConfigProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.WebDataBinder;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WebLogSearchControllerTest {

    private LogService logService;
    private ConfigProperties configProperties;

    private WebLogSearchController controller;

    @Before
    public void setUp() {
        logService = mock(LogService.class);
        configProperties = mock(ConfigProperties.class);

        controller = new WebLogSearchController(logService, configProperties);
    }

    @Test
    public void initBinder() {
        WebDataBinder binder = mock(WebDataBinder.class);

        when(binder.getTarget()).thenReturn(new SearchInfo());
        controller.initBinder(binder);

        when(binder.getTarget()).thenReturn(new SearchInfoResult());
        controller.initBinder(binder);
    }

    @Test
    public void populateTypes() {
        List<FileExtension> expected = Arrays.asList(FileExtension.values());

        assertEquals(expected, controller.populateTypes());
    }

    @Test
    public void logSearch() {
    }

    @Test
    public void logSearch1() {
    }

    @Test
    public void addRow() {
    }

    @Test
    public void removeRow() {
    }
}