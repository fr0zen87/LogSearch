package com.example.logsearch.entities;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Component
public class SearchInfoValidator {

    public void validate(SearchInfo searchInfo, SearchInfoResult searchInfoResult) {
        try {
            Pattern.compile(searchInfo.getRegularExpression()).asPredicate();
        } catch (Exception e) {
            searchInfoResult.setErrorCode(44L);
            searchInfoResult.setErrorMessage("Incorrect resource name");
            return;
        }
        if (searchInfo.getRegularExpression() == null ||
                searchInfo.getDateInterval() == null) {
            searchInfoResult.setErrorCode(37L);
            searchInfoResult.setErrorMessage("Missed mandatory parameter");
            return;
        }
        for (SignificantDateInterval interval : searchInfo.getDateInterval()) {
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
