package com.example.logsearch.utils;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SignificantDateInterval;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileSearchTest {

    private ConfigProperties configProperties;
    private SearchInfo searchInfo;
    List<SignificantDateInterval> intervals;

    private FileSearch fileSearch;

    @Before
    public void setUp() {
        searchInfo = new SearchInfo();
        searchInfo.setRegularExpression("");
        searchInfo.setLocation("C:\\Users\\APZhukov\\IdeaProjects\\domains");
        searchInfo.setRealization(true);
        searchInfo.setFileExtension(FileExtension.DOC);
        intervals = new ArrayList<>();
        searchInfo.setDateIntervals(intervals);

        configProperties = mock(ConfigProperties.class);

        fileSearch = new FileSearch(configProperties);
    }

    @Test
    public void validFileSearch() {
        LocalDateTime from = LocalDateTime.of(2018,3,1,0,0);
        LocalDateTime to = LocalDateTime.of(2018,4,1,0,0);
        SignificantDateInterval interval = new SignificantDateInterval(from, to);
        searchInfo.getDateIntervals().add(interval);

        String res = "src/test/java/com/example/logsearch/utils/test_data/valid_doc";
        when(configProperties.getPath()).thenReturn(res);

        assertTrue(fileSearch.fileSearch(searchInfo));
        verify(configProperties).getPath();
    }

    @Test
    public void invalidFileSearch() {
        LocalDateTime from = LocalDateTime.of(2018,3,1,0,0);
        LocalDateTime to = LocalDateTime.of(2018,4,1,0,0);
        SignificantDateInterval interval = new SignificantDateInterval(from, to);
        searchInfo.getDateIntervals().add(interval);

        String res = "src/test/java/com/example/logsearch/utils/test_data/invalid_docs";
        when(configProperties.getPath()).thenReturn(res);

        assertFalse(fileSearch.fileSearch(searchInfo));
        verify(configProperties).getPath();
    }
}