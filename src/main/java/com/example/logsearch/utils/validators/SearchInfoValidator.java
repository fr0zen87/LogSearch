package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.CorrectionCheckResult;
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

    public SearchInfoResult validate(SearchInfo searchInfo) {

        if (searchInfo.isRealization() && searchInfo.getFileExtension() == null) {
            SearchInfoResult searchInfoResult = new SearchInfoResult();
            searchInfoResult.setErrorCode(CorrectionCheckResult.ERROR3701.getErrorCode());
            searchInfoResult.setErrorMessage(CorrectionCheckResult.ERROR3701.getErrorMessage());
            return searchInfoResult;
        }

        Path defaultPath = Paths.get(System.getProperty("user.dir")).getParent().getParent();
        Path path = Paths.get(defaultPath.toString(), searchInfo.getLocation());
        if (!Files.exists(path)) {
            SearchInfoResult searchInfoResult = new SearchInfoResult();
            searchInfoResult.setErrorCode(CorrectionCheckResult.ERROR44.getErrorCode());
            searchInfoResult.setErrorMessage(CorrectionCheckResult.ERROR44.getErrorMessage());
            return searchInfoResult;
        }

        if (searchInfo.getRegularExpression() == null ||
                searchInfo.getDateIntervals() == null) {
            SearchInfoResult searchInfoResult = new SearchInfoResult();
            searchInfoResult.setErrorCode(CorrectionCheckResult.ERROR37.getErrorCode());
            searchInfoResult.setErrorMessage(CorrectionCheckResult.ERROR37.getErrorMessage());
            return searchInfoResult;
        }

        for (SignificantDateInterval interval : searchInfo.getDateIntervals()) {
            if (interval.getDateFrom() == null) interval.setDateFrom(LocalDateTime.MIN);
            if (interval.getDateTo() == null) interval.setDateTo(LocalDateTime.MAX);
            try {
                LocalDateTime.parse(interval.getDateFrom().toString());
                LocalDateTime.parse(interval.getDateTo().toString());
            } catch (DateTimeParseException e) {
                SearchInfoResult searchInfoResult = new SearchInfoResult();
                searchInfoResult.setErrorCode(CorrectionCheckResult.ERROR19.getErrorCode());
                searchInfoResult.setErrorMessage(CorrectionCheckResult.ERROR19.getErrorMessage());
                return searchInfoResult;
            }

            if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
                SearchInfoResult searchInfoResult = new SearchInfoResult();
                searchInfoResult.setErrorCode(CorrectionCheckResult.ERROR18.getErrorCode());
                searchInfoResult.setErrorMessage(CorrectionCheckResult.ERROR18.getErrorMessage());
                return searchInfoResult;
            }

            if (interval.getDateFrom().isAfter(interval.getDateTo())) {
                SearchInfoResult searchInfoResult = new SearchInfoResult();
                searchInfoResult.setErrorCode(CorrectionCheckResult.ERROR1.getErrorCode());
                searchInfoResult.setErrorMessage(CorrectionCheckResult.ERROR1.getErrorMessage());
                return searchInfoResult;
            }
        }
        return null;
    }
}
