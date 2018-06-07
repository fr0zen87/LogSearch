package com.example.logsearch.utils;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SignificantDateInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class FileSearch {

    private static final String EXCEPTION = "Exception raised: {}";

    private final Logger logger = LoggerFactory.getLogger(FileSearch.class);

    private boolean isFileFound;
    private final ConfigProperties configProperties;

    @Autowired
    public FileSearch(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public boolean fileSearch(SearchInfo searchInfo) {

        logger.info("Starting file search");
        long start = System.currentTimeMillis();

        isFileFound = false;
        Path path = Paths.get(configProperties.getPath());
        if (!path.toFile().exists()) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                logger.error(EXCEPTION, e.getMessage());
            }
        }

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                    try {
                        UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
                        if (view == null || !view.list().contains("info")) {
                            return FileVisitResult.CONTINUE;
                        }
                        byte[] bytes = (byte[]) Files.getAttribute(file, "user:info");
                        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                        SearchInfo fileSearchInfo = (SearchInfo) objectInputStream.readObject();

                        objectInputStream.close();

                        if (searchInfo.getRegularExpression().equals(fileSearchInfo.getRegularExpression()) &&
                                searchInfo.getFileExtension().value().equals(fileSearchInfo.getFileExtension().value()) &&
                                searchInfo.getLocation().equals(fileSearchInfo.getLocation()) &&
                                compareDates(searchInfo.getDateIntervals(), fileSearchInfo.getDateIntervals())) {
                            isFileFound = true;
                            configProperties.setFileLink(file.toString());
                            return FileVisitResult.TERMINATE;
                        }
                    } catch (Exception e) {
                        logger.error(EXCEPTION, Arrays.toString(e.getStackTrace()));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.error(EXCEPTION, Arrays.toString(e.getStackTrace()));
        }

        logger.info("File search finished in {} ms", (System.currentTimeMillis() - start));

        return isFileFound;
    }

    private boolean compareDates(List<SignificantDateInterval> currentIntervals,
                                 List<SignificantDateInterval> fileIntervals) {
        int count = 0;
        for (SignificantDateInterval currentInterval : currentIntervals) {
            for (SignificantDateInterval fileInterval : fileIntervals) {
                if (!fileInterval.getDateFrom().isAfter(currentInterval.getDateFrom()) ||
                        !fileInterval.getDateTo().isBefore(currentInterval.getDateTo())) {

                    long currentIntervalDifference = Duration.between(currentInterval.getDateTo(), currentInterval.getDateFrom()).toMinutes();
                    long fileIntervalDifference = Duration.between(fileInterval.getDateTo(), fileInterval.getDateFrom()).toMinutes();

                    if (fileIntervalDifference / currentIntervalDifference <= 1.1) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count == currentIntervals.size();
    }
}
