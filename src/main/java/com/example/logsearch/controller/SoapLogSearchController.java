package com.example.logsearch.controller;

import com.example.logsearch.entities.ObjectResponse;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.service.LogService;
import com.example.logsearch.utils.ConfigProperties;
import com.example.logsearch.utils.validators.SearchInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SoapLogSearchController {

    private static final String NAMESPACE_URI = "http://entities.logsearch.example.com";

    private final LogService logService;
    private final ConfigProperties configProperties;
    private final SearchInfoValidator validator;

    @Autowired
    public SoapLogSearchController(LogService logService,
                                   ConfigProperties configProperties,
                                   SearchInfoValidator validator) {
        this.logService = logService;
        this.configProperties = configProperties;
        this.validator = validator;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchInfo")
    @ResponsePayload
    public ObjectResponse logSearch(@RequestPayload SearchInfo searchInfo) {

        SearchInfoResult searchInfoResult = validator.validate(searchInfo);
        if (searchInfoResult != null) {
            return new ObjectResponse(searchInfoResult);
        }
        if(searchInfo.isRealization()) {
            logService.logSearch(searchInfo);
            String link = configProperties.getFileLink().replaceAll("\\\\", "/");
            return new ObjectResponse(link);
        }
        return new ObjectResponse(logService.logSearch(searchInfo));
    }
}
