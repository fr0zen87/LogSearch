package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.example.logsearch.entities.CorrectionCheckResult.*;

@Component
public class SearchInfoValidator {

    public SearchInfoResult validate(SearchInfo searchInfo) {

        if (searchInfo.isRealization() && searchInfo.getFileExtension() == null) {
            return new SearchInfoResult(ERROR3701.getErrorCode(), ERROR3701.getErrorMessage());
        }

        Path defaultPath = Paths.get(System.getProperty("user.dir")).getParent().getParent();
        Path path = Paths.get(defaultPath.toString(), searchInfo.getLocation());
        if (!Files.exists(path)) {
            return new SearchInfoResult(ERROR44.getErrorCode(), ERROR44.getErrorMessage());
        }

        if (searchInfo.getRegularExpression() == null || searchInfo.getDateIntervals() == null) {
            return new SearchInfoResult(ERROR37.getErrorCode(), ERROR37.getErrorMessage());
        }

        for (SignificantDateInterval interval : searchInfo.getDateIntervals()) {
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
