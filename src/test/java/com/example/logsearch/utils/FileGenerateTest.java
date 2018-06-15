package com.example.logsearch.utils;

import com.example.logsearch.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileGenerateTest {

    private ConfigProperties configProperties;
    private LogsSearch search;
    private ResourceLoader resourceLoader;
    private SearchInfo searchInfo;
    private File fileToGenerate;

    private FileGenerate fileGenerate;

    @Before
    public void setUp() {
        searchInfo = new SearchInfo();
        searchInfo.setRegularExpression("");
        searchInfo.setLocation(System.getProperty("user.dir"));
        searchInfo.setRealization(false);
        searchInfo.setFileExtension(FileExtension.DOC);
        List<SignificantDateInterval> intervals = new ArrayList<>();
        intervals.add(new SignificantDateInterval());
        searchInfo.setDateIntervals(intervals);

        configProperties = mock(ConfigProperties.class);
        search = mock(LogsSearch.class);
        resourceLoader = new DefaultResourceLoader();

        fileGenerate = new FileGenerate(configProperties, search, resourceLoader);
    }

    @After
    public void tearDown() {
        try {
            Files.deleteIfExists(fileToGenerate.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileGenerate() {
        SearchInfoResult result = new SearchInfoResult();
        result.setEmptyResultMessage("empty");

        when(search.logSearch(searchInfo)).thenReturn(result);

        fileToGenerate = new File(System.getProperty("user.dir"), "result_log.doc");
        fileGenerate.fileGenerate(searchInfo, fileToGenerate);

        verify(search).logSearch(searchInfo);
    }

    @Test
    public void savePdfTest() {
        searchInfo.setFileExtension(FileExtension.PDF);
        SearchInfoResult result = new SearchInfoResult();
        result.setEmptyResultMessage("empty");

        when(search.logSearch(searchInfo)).thenReturn(result);

        fileToGenerate = new File(System.getProperty("user.dir"), "result_log.pdf");
        fileGenerate.fileGenerate(searchInfo, fileToGenerate);

        verify(search).logSearch(searchInfo);
    }

    @Test
    public void saveRtfTest() {
        searchInfo.setFileExtension(FileExtension.RTF);
        SearchInfoResult result = new SearchInfoResult();
        result.setEmptyResultMessage("empty");

        when(search.logSearch(searchInfo)).thenReturn(result);

        fileToGenerate = new File(System.getProperty("user.dir"), "result_log.rtf");
        fileGenerate.fileGenerate(searchInfo, fileToGenerate);

        verify(search).logSearch(searchInfo);
    }

    @Test
    public void generateUniqueFile() {

        when(configProperties.getPath()).thenReturn(System.getProperty("user.dir"));
        fileToGenerate = new File(System.getProperty("user.dir"), "result_log.xml");
        assertEquals(fileToGenerate, fileGenerate.generateUniqueFile(FileExtension.XML));

        try {
            Files.createFile(fileToGenerate.toPath());
            File expected2 = new File(System.getProperty("user.dir"), "result_log_1.xml");
            assertEquals(expected2, fileGenerate.generateUniqueFile(FileExtension.XML));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}