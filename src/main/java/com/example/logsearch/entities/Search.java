package com.example.logsearch.entities;

import com.example.logsearch.utils.SearchInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Search {

    private ResultLogs resultLogs;
    private SearchInfoResult searchInfoResult;
    private final SearchInfoValidator validator;

    @Autowired
    public Search(SearchInfoValidator validator) {
        this.validator = validator;
    }

    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        List<SignificantDateInterval> dateInterval = searchInfo.getDateInterval();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        searchInfoResult = new SearchInfoResult();
        validator.validate(searchInfo, searchInfoResult);
        if (searchInfoResult.getErrorCode() != null) {
            return searchInfoResult;
        }

        Path logFilePath;
        Path defaultPath = Paths.get(System.getProperty("user.dir")).getParent().getParent();
        if (searchInfo.getLocation() != null) {
            logFilePath = Paths.get(defaultPath.toString(), searchInfo.getLocation());
        } else {
            logFilePath = defaultPath;
        }

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.log*");
        Pattern accessLogPattern = Pattern.compile("access.log*");

        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                Path name = file.getFileName();

                if (accessLogPattern.matcher(name.toString()).find()) {
                    return FileVisitResult.CONTINUE;
                }

                if (matcher.matches(name) && checkFileCreationTime(attr, dateInterval)) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        List<String> fileLines = new ArrayList<>();
                        for (String line : Files.readAllLines(file, StandardCharsets.ISO_8859_1)) {
                            if (!line.trim().endsWith(">")) {
                                sb = sb.length() == 0 ? sb.append(line) : sb.append(", ").append(line);
                            } else {
                                sb.append(line.trim());
                                fileLines.add(sb.toString());
                                sb = new StringBuilder();
                            }
                        }
                        List<ResultLogs> resultLogsArray = fileLines.stream()
                                .parallel()
                                .filter(Pattern.compile(searchInfo.getRegularExpression()).asPredicate())
                                .map(line -> {
                                    LocalDateTime parsedDate = LocalDateTime.parse(line.substring(5, 24), formatter);
                                    resultLogs = new ResultLogs();

                                    for (SignificantDateInterval interval : dateInterval) {
                                        if ((parsedDate.isAfter(interval.getDateFrom())
                                                || parsedDate.isEqual(interval.getDateFrom()))
                                                && parsedDate.isBefore(interval.getDateTo())
                                                ) {
                                            String content = line.substring(34);

                                            resultLogs.setTimeMoment(parsedDate);
                                            resultLogs.setContent(content);
                                            resultLogs.setFileName(file.toString());
                                        }
                                    }
                                    return resultLogs;
                                })
                                .filter(log -> log.fileName != null)
                                .sorted(Comparator.comparing(ResultLogs::getTimeMoment))
                                .collect(Collectors.toList());
                        if (!resultLogsArray.isEmpty()) {
                            searchInfoResult.getResultLogs().addAll(resultLogsArray);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(logFilePath, matcherVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (searchInfoResult.getResultLogs().isEmpty()) {
            searchInfoResult.setEmptyResultMessage("No logs found");
        }
        return searchInfoResult;
    }

    private boolean checkFileCreationTime(BasicFileAttributes attr, List<SignificantDateInterval> dateInterval) {

        LocalDateTime fileCreationTime = LocalDateTime
                .ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
        LocalDateTime fileModificationTime = LocalDateTime
                .ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

        for (SignificantDateInterval interval : dateInterval) {
            if (fileCreationTime.isBefore(interval.getDateTo())
                    && fileModificationTime.isAfter(interval.getDateFrom())) {
                return true;
            }
        }
        return false;
    }
}
