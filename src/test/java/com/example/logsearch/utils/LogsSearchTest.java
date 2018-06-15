package com.example.logsearch.utils;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogsSearchTest {

    private SearchInfo searchInfo;
    private List<SignificantDateInterval> intervals;

    private LogsSearch search;

    @Before
    public void setUp() {
        searchInfo = new SearchInfo();
        searchInfo.setRegularExpression("");
        searchInfo.setRealization(false);
        searchInfo.setFileExtension(FileExtension.DOC);
        searchInfo.setLocation("src/test/java/com/example/logsearch/utils/test_data/logs");
        intervals = new ArrayList<>();
        searchInfo.setDateIntervals(intervals);

        search = new LogsSearch();
    }

    @Test
    public void invalidIntervalSearch() {
        LocalDateTime from = LocalDateTime.of(2017,1,1,1,1);
        LocalDateTime to = LocalDateTime.of(2017,2,1,1,1);
        intervals.add(new SignificantDateInterval(from, to));

        SearchInfoResult expected = new SearchInfoResult();
        expected.setEmptyResultMessage("No logs found");

        assertEquals(expected, search.logSearch(searchInfo));
    }

    @Test
    public void validTest() {
        intervals.add(new SignificantDateInterval(LocalDateTime.MIN, LocalDateTime.MAX));
        SearchInfoResult expected = search.logSearch(searchInfo);
        assertNull(expected.getEmptyResultMessage());
        assertNotNull(expected.getResultLogs());
    }
}