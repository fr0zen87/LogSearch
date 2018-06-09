package com.example.logsearch.service;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.utils.FileGenerate;
import com.example.logsearch.utils.FileSearch;
import com.example.logsearch.utils.LogsSearch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LogServiceImplTest {

    private LogsSearch search;
    private FileSearch fileSearch;
    private FileGenerate fileGenerate;
    private SearchInfo searchInfo;

    private LogServiceImpl service;

    @Before
    public void setUp() {
        search = mock(LogsSearch.class);
        fileSearch = mock(FileSearch.class);
        fileGenerate = mock(FileGenerate.class);
        searchInfo = mock(SearchInfo.class);

        service = new LogServiceImpl(search, fileSearch, fileGenerate);
    }

    @Test
    public void logSearchWithRealization() {
        when(searchInfo.isRealization()).thenReturn(true);
        when(fileSearch.fileSearch(searchInfo)).thenReturn(true);

        assertNull(service.logSearch(searchInfo));
    }

    @Test
    public void logSearchWithRealizationAndNotFound() {

        File file = new File("");

        when(searchInfo.isRealization()).thenReturn(true);
        when(fileSearch.fileSearch(searchInfo)).thenReturn(false);
        when(searchInfo.getFileExtension()).thenReturn(FileExtension.DOC);
        when(fileGenerate.generateUniqueFile(searchInfo.getFileExtension())).thenReturn(file);
        doNothing().when(fileGenerate).fileGenerate(searchInfo, file);

        assertNull(service.logSearch(searchInfo));
    }

    @Test
    public void logSearchWithoutRealization() {
        when(searchInfo.isRealization()).thenReturn(false);
        when(search.logSearch(searchInfo)).thenReturn(null);

        assertNull(service.logSearch(searchInfo));
    }

    @Test
    public void fileSearch() {
        when(fileSearch.fileSearch(searchInfo)).thenReturn(true);

        assertTrue(service.fileSearch(searchInfo));
    }

    @Test
    public void fileGenerate() {
        File file = new File("");
        doNothing().when(fileGenerate).fileGenerate(searchInfo, file);

        service.fileGenerate(searchInfo, file);
    }
}