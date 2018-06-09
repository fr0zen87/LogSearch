package com.example.logsearch.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectResponseTest {

    private ObjectResponse objectResponse = new ObjectResponse();
    private SearchInfoResult searchInfoResult = new SearchInfoResult();

    @Before
    public void setUp() {
        objectResponse.setLink("link");
        objectResponse.setSearchInfoResult(searchInfoResult);

        ObjectResponse linkResponse = new ObjectResponse("link");
        ObjectResponse resultResponse = new ObjectResponse(searchInfoResult);
    }

    @Test
    public void getLink() {
        assertEquals("link", objectResponse.getLink());
    }

    @Test
    public void getSearchInfoResult() {
        assertEquals(searchInfoResult, objectResponse.getSearchInfoResult());
    }
}