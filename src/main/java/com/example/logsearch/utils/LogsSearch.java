package com.example.logsearch.utils;

import com.example.logsearch.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class LogsSearch {

    private final Logger logger = LoggerFactory.getLogger(LogsSearch.class);

    public SearchInfoResult logSearch(SearchInfo searchInfo) {

        logger.info("Starting logs search");
        long start = System.currentTimeMillis();

        List<SignificantDateInterval> dateInterval = searchInfo.getDateIntervals();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        SearchInfoResult searchInfoResult = new SearchInfoResult();

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
                                    ResultLogs resultLogs = new ResultLogs();

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
                                .filter(log -> log.getFileName() != null)
                                .collect(Collectors.toList());
                        if (!resultLogsArray.isEmpty()) {
                            searchInfoResult.getResultLogs().addAll(resultLogsArray);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("Exception raised: " + Arrays.toString(e.getStackTrace()));
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(Paths.get(searchInfo.getLocation()), matcherVisitor);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Exception raised: " + Arrays.toString(e.getStackTrace()));
        }
        if (searchInfoResult.getResultLogs().isEmpty()) {
            searchInfoResult.setEmptyResultMessage("No logs found");
        } else {
            searchInfoResult.getResultLogs().sort(Comparator.comparing(ResultLogs::getTimeMoment));
        }

        logger.info("Logs search finished in " + (System.currentTimeMillis() - start) + " ms");

        return searchInfoResult;
    }

    private boolean checkFileCreationTime(BasicFileAttributes attr, List<SignificantDateInterval> dateInterval) {

        LocalDateTime fileCreationTime = LocalDateTime
                .ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
        LocalDateTime fileModificationTime = LocalDateTime
                .ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

        return dateInterval.stream().anyMatch(interval -> fileCreationTime.isBefore(interval.getDateTo())
                && fileModificationTime.isAfter(interval.getDateFrom()));
    }
}
