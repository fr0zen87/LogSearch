package com.example.logsearch.utils;

import com.example.logsearch.entities.FileExtension;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileGenerateTest {

    private ConfigProperties configProperties;
    private LogsSearch search;
    private ResourceLoader resourceLoader;

    private FileGenerate fileGenerate;

    @Before
    public void setUp() {
        configProperties = mock(ConfigProperties.class);
        search = mock(LogsSearch.class);
        resourceLoader = mock(ResourceLoader.class);

        fileGenerate = new FileGenerate(configProperties, search, resourceLoader);
    }

    @Test
    public void fileGenerate() {
    }

    @Test
    public void generateUniqueFile() {

        when(configProperties.getPath()).thenReturn(System.getProperty("user.dir"));
        File expected = new File(System.getProperty("user.dir"), "result_log.xml");
        assertEquals(expected, fileGenerate.generateUniqueFile(FileExtension.XML));

        try {
            Files.createFile(expected.toPath());
            File expected2 = new File(System.getProperty("user.dir"), "result_log_1.xml");
            assertEquals(expected2, fileGenerate.generateUniqueFile(FileExtension.XML));
            Files.deleteIfExists(expected.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}