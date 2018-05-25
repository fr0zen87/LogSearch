package com.example.logsearch.controller;

import com.example.logsearch.entities.ObjectResponse;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.service.LogService;
import com.example.logsearch.utils.ConfigProperties;
import com.example.logsearch.utils.SearchInfoValidator;
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

        ObjectResponse response = null;
        SearchInfoResult searchInfoResult = validator.validate(searchInfo);
        if (searchInfoResult != null) {
            response = new ObjectResponse();
            response.setSearchInfoResult(searchInfoResult);
            return response;
        }
        if(searchInfo.isRealization()) {
            logService.logSearch(searchInfo);
            String link = configProperties.getFileLink().replaceAll("\\\\", "/");
            response = new ObjectResponse();
            response.setLink(link);
            return response;
        }
        response = new ObjectResponse();
        response.setSearchInfoResult(logService.logSearch(searchInfo));
        return response;
    }
}
