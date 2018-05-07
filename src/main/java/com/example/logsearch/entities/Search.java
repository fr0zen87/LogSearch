package com.example.logsearch.entities;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Search {

    private ResultLogs resultLogs;
    private SearchInfoResult searchInfoResult;

    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        List<SignificantDateInterval> dateInterval = searchInfo.getDateInterval();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        searchInfoResult = new SearchInfoResult();

        Path logFilePath = Paths.get(searchInfo.getLocation());
        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher("glob:*.log");

        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                Path name = file.getFileName();

                if(name.endsWith("access.log")){
                    return FileVisitResult.CONTINUE;
                }
                if (matcher.matches(name)) {
                    try {
                        List<ResultLogs> resultLogsArray = Files.lines(file)
                                .filter(line -> line.startsWith("####"))
                                .filter(Pattern.compile(searchInfo.getRegularExpression()).asPredicate())
                                .map(elem -> {
                                    LocalDateTime parsedDate = LocalDateTime.parse(elem.substring(5, 24), formatter);
                                    resultLogs = new ResultLogs();

                                    for(SignificantDateInterval interval: dateInterval) {
                                        if ((parsedDate.isAfter(interval.getDateFrom())
                                                || parsedDate.isEqual(interval.getDateFrom()))
                                                && parsedDate.isBefore(interval.getDateTo())
                                                ) {
                                            String content = elem.substring(33);

                                            resultLogs.setTimeMoment(parsedDate);
                                            resultLogs.setContent(content);
                                            resultLogs.setFileName(file.toString());
                                        }
                                    }
                                    return resultLogs;})
                                .filter(log -> log.fileName != null)
                                .collect(Collectors.toList());
                        if(resultLogsArray == null || resultLogsArray.isEmpty()){
                            searchInfoResult.setEmptyResultMessage("No logs found");
                        }
                        else{
                            searchInfoResult.setResultLogs(resultLogsArray);
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
        return searchInfoResult;
    }
}
