package com.example.logsearch.utils;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Component
public class SearchInfoValidator {

    public void validate(SearchInfo searchInfo, SearchInfoResult searchInfoResult) {

        Path defaultPath = Paths.get(System.getProperty("user.dir")).getParent().getParent();
        Path path = Paths.get(defaultPath.toString(), searchInfo.getLocation());
        if (!Files.exists(path)) {
            searchInfoResult.setErrorCode(44L);
            searchInfoResult.setErrorMessage("Incorrect resource name");
            return;
        }
        if (searchInfo.getRegularExpression() == null ||
                searchInfo.getDateIntervals() == null) {
            searchInfoResult.setErrorCode(37L);
            searchInfoResult.setErrorMessage("Missed mandatory parameter");
            return;
        }
        for (SignificantDateInterval interval : searchInfo.getDateIntervals()) {
            if (interval.getDateFrom() == null) interval.setDateFrom(LocalDateTime.MIN);
            if (interval.getDateTo() == null) interval.setDateTo(LocalDateTime.MAX);
            try {
                LocalDateTime.parse(interval.getDateFrom().toString());
                LocalDateTime.parse(interval.getDateTo().toString());
            } catch (DateTimeParseException e) {
                searchInfoResult.setErrorCode(19L);
                searchInfoResult.setErrorMessage("Incorrect time format");
                return;
            }
            if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
                searchInfoResult.setErrorCode(18L);
                searchInfoResult.setErrorMessage("DateFrom exceeds PresentTime");
                return;
            }
            if (interval.getDateFrom().isAfter(interval.getDateTo())) {
                searchInfoResult.setErrorCode(1L);
                searchInfoResult.setErrorMessage("DateFrom exceeds DateTo");
                return;
            }
        }
    }
}
