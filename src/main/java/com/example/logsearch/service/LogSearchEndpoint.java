package com.example.logsearch.service;

import com.example.logsearch.entities.Search;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.utils.SearchInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class LogSearchEndpoint implements LogService {

    private static final String NAMESPACE_URI = "http://entities.logsearch.example.com";

    private final Search search;
    private final SearchInfoValidator validator;

    @Autowired
    public LogSearchEndpoint(Search search, SearchInfoValidator validator) {
        this.search = search;
        this.validator = validator;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchInfo")
    @ResponsePayload
    public SearchInfoResult logSearch(@RequestPayload SearchInfo searchInfo) {
        SearchInfoResult searchInfoResult = validator.validate(searchInfo);
        if (searchInfoResult != null) {
            return searchInfoResult;
        }
        if (searchInfo.isRealization()) {
            if (!fileSearch(searchInfo)) {
                fileGenerate(searchInfo);
            }
            return null;
        }
        return search.logSearch(searchInfo);
    }

    @Override
    public boolean fileSearch(SearchInfo searchInfo) {
        return search.fileSearch(searchInfo);
    }

    @Override
    public void fileGenerate(SearchInfo searchInfo) {
        search.fileGenerate(searchInfo);
    }
}
