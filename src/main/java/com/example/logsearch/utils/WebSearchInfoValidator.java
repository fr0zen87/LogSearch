package com.example.logsearch.utils;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SignificantDateInterval;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class WebSearchInfoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchInfo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SearchInfo searchInfo = (SearchInfo) target;
        if (searchInfo.isRealization() && searchInfo.getFileExtension() == null) {
            errors.rejectValue("fileExtension", CorrectionCheckResult.ERROR3701.getErrorMessage());
        }

        Path defaultPath = Paths.get(System.getProperty("user.dir")).getParent().getParent();
        Path path = Paths.get(defaultPath.toString(), searchInfo.getLocation());
        if (!Files.exists(path)) {
            errors.rejectValue("location", String.valueOf(CorrectionCheckResult.ERROR44.getErrorCode()), CorrectionCheckResult.ERROR44.getErrorMessage());
        }

        if (searchInfo.getRegularExpression() == null || searchInfo.getDateIntervals() == null) {
            errors.rejectValue("regularExpression", CorrectionCheckResult.ERROR37.getErrorMessage());
        }

        List<SignificantDateInterval> intervals = searchInfo.getDateIntervals();
        if (intervals.isEmpty()) {
            intervals.add(new SignificantDateInterval());
            intervals.get(0).setDateFrom(LocalDateTime.MIN);
            intervals.get(0).setDateTo(LocalDateTime.MAX);
        } else {
            for (SignificantDateInterval interval : intervals) {
                if (interval.getDateFrom() == null) interval.setDateFrom(LocalDateTime.MIN);
                if (interval.getDateTo() == null) interval.setDateTo(LocalDateTime.MAX);
                try {
                    LocalDateTime.parse(interval.getDateFrom().toString());
                    LocalDateTime.parse(interval.getDateTo().toString());
                } catch (DateTimeParseException e) {
                    errors.rejectValue("dateFormat", CorrectionCheckResult.ERROR19.getErrorMessage());
                }

                if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
                    errors.rejectValue("exceedsPresentTime", CorrectionCheckResult.ERROR18.getErrorMessage());
                }

                if (interval.getDateFrom().isAfter(interval.getDateTo())) {
                    errors.rejectValue("exceedsDateTo", CorrectionCheckResult.ERROR1.getErrorMessage());
                }
            }
        }
    }
}
