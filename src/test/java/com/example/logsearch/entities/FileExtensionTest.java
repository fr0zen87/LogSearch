package com.example.logsearch.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileExtensionTest {

    @Test
    public void value() {
        assertEquals(FileExtension.DOC, FileExtension.valueOf("DOC"));
        assertNotEquals(FileExtension.PDF, FileExtension.valueOf("DOC"));
    }
}