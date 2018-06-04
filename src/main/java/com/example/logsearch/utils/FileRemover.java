package com.example.logsearch.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileRemover {

    private final ConfigProperties configProperties;

    @Autowired
    public FileRemover(@Qualifier(value = "configProperties") ConfigProperties properties) {
        this.configProperties = properties;
    }

    @Scheduled(fixedRateString = "#{${logs.deletionInterval} * 24 * 60 * 60 * 1000}")
    private void removeFiles() {

        try {
            String filesDir = configProperties.getPath();
            long fileExistTime = configProperties.getFileExistTime() * 24 * 60 * 60 * 1000;

            Files.list(Paths.get(filesDir))
                    .map(Path::toFile)
                    .filter(file -> (System.currentTimeMillis() - file.lastModified()) > fileExistTime)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
