package com.example.logsearch.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileExtensionTest {

    @Test
    public void value() {
        assertEquals("DOC", FileExtension.DOC.value());
        assertNotEquals("PDF", FileExtension.DOC.value());
    }
}