package com.example.logsearch.service;

import com.example.logsearch.entities.Search;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class LogSearchEndpoint implements LogService {

    private static final String NAMESPACE_URI = "http://entities.logsearch.example.com";

    private final Search search;

    @Autowired
    public LogSearchEndpoint(Search search) {
        this.search = search;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SearchInfo")
    @ResponsePayload
    public SearchInfoResult logSearch(@RequestPayload SearchInfo searchInfo) {
        return search.logSearch(searchInfo);
    }
}
