package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.example.logsearch.entities.CorrectionCheckResult.*;

@Component
public class SearchInfoValidator {

    public SearchInfoResult validate(SearchInfo searchInfo) {

        if (searchInfo.isRealization() && searchInfo.getFileExtension() == null) {
            return new SearchInfoResult(ERROR3701.getErrorCode(), ERROR3701.getErrorMessage());
        }

        Path domainPath = Paths.get(System.getProperty("user.dir"));
        while (!domainPath.endsWith("domains")) {
            domainPath = domainPath.getParent();
        }
        if (searchInfo.getLocation() == null) {
            searchInfo.setLocation(String.valueOf(domainPath));
        } else {
            Path locationPath = Paths.get(String.valueOf(domainPath), searchInfo.getLocation());
            if (!locationPath.toFile().exists()) {
                return new SearchInfoResult(ERROR44.getErrorCode(), ERROR44.getErrorMessage());
            }
            searchInfo.setLocation(String.valueOf(locationPath));
        }

        if (searchInfo.getRegularExpression() == null || searchInfo.getDateIntervals() == null) {
            return new SearchInfoResult(ERROR37.getErrorCode(), ERROR37.getErrorMessage());
        }

        List<SignificantDateInterval> intervals = searchInfo.getDateIntervals();

        return getSearchInfoResult(intervals);
    }

    private SearchInfoResult getSearchInfoResult(List<SignificantDateInterval> intervals) {
        if (intervals.isEmpty()) {
            intervals.add(new SignificantDateInterval(LocalDateTime.MIN, LocalDateTime.MAX));
        }
        for (SignificantDateInterval interval : intervals) {
            if (interval.getDateFrom() == null) {
                interval.setDateFrom(LocalDateTime.MIN);
            }
            if (interval.getDateTo() == null) {
                interval.setDateTo(LocalDateTime.MAX);
            }
            try {
                LocalDateTime.parse(interval.getDateFrom().toString());
                LocalDateTime.parse(interval.getDateTo().toString());
            } catch (DateTimeParseException e) {
                return new SearchInfoResult(ERROR19.getErrorCode(), ERROR19.getErrorMessage());
            }

            if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
                return new SearchInfoResult(ERROR18.getErrorCode(), ERROR18.getErrorMessage());
            }

            if (interval.getDateFrom().isAfter(interval.getDateTo())) {
                return new SearchInfoResult(ERROR1.getErrorCode(), ERROR1.getErrorMessage());
            }
        }
        return null;
    }
}
