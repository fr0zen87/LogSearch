package com.example.logsearch.utils.validators;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SignificantDateInterval;
import com.example.logsearch.utils.ConfigProperties;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.example.logsearch.entities.CorrectionCheckResult.*;

public class WebSearchInfoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchInfo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SearchInfo searchInfo = (SearchInfo) target;
        if (searchInfo.isRealization() && searchInfo.getFileExtension() == null) {
            errors.rejectValue("fileExtension", ERROR3701.getErrorMessage());
        }

        Path domainPath = ConfigProperties.getDomainPath();
        if (searchInfo.getLocation() == null) {
            searchInfo.setLocation(String.valueOf(domainPath));
        } else {
            Path locationPath = Paths.get(String.valueOf(domainPath), searchInfo.getLocation());
            if (!locationPath.toFile().exists()) {
                errors.rejectValue("location", ERROR44.getErrorMessage());
            }
            searchInfo.setLocation(String.valueOf(locationPath));
        }

        if (searchInfo.getRegularExpression() == null || searchInfo.getDateIntervals() == null) {
            errors.rejectValue("regularExpression", ERROR37.getErrorMessage());
        }

        List<SignificantDateInterval> intervals = searchInfo.getDateIntervals();
        checkDateIntervals(errors, intervals);
    }

    private void checkDateIntervals(Errors errors, List<SignificantDateInterval> intervals) {
        SearchInfoValidator.fillIntervals(intervals);
        for (SignificantDateInterval interval : intervals) {
            try {
                LocalDateTime.parse(interval.getDateFrom().toString());
                LocalDateTime.parse(interval.getDateTo().toString());
            } catch (DateTimeParseException e) {
                errors.rejectValue("dateFormat", ERROR19.getErrorMessage());
            }

            if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
                errors.rejectValue("exceedsPresentTime", ERROR18.getErrorMessage());
            }

            if (interval.getDateFrom().isAfter(interval.getDateTo())) {
                errors.rejectValue("exceedsDateTo", ERROR1.getErrorMessage());
            }
        }
    }
}
