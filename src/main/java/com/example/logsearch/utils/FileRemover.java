package com.example.logsearch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class FileRemover {

    private final Logger logger = LoggerFactory.getLogger(FileRemover.class);
    private final ConfigProperties configProperties;

    @Autowired
    public FileRemover(@Qualifier(value = "configProperties") ConfigProperties properties) {
        this.configProperties = properties;
    }

    @Scheduled(fixedRateString = "#{${logs.deletionInterval} * 24 * 60 * 60 * 1000}")
    private void removeFiles() {

        try {
            String filesDir = configProperties.getPath();
            long fileExistTime = configProperties.getFileExistTime() * 24 * 60 * 60 * 1000L;

            if(!Paths.get(filesDir).toFile().exists()) {
                Files.createDirectory(Paths.get(filesDir));
            }
            Files.list(Paths.get(filesDir))
                    .map(Path::toFile)
                    .filter(file -> (System.currentTimeMillis() - file.lastModified()) > fileExistTime)
                    .forEach(File::delete);
        } catch (IOException e) {
            logger.error("Exception raised: {}", Arrays.toString(e.getStackTrace()));
        }
    }
}
