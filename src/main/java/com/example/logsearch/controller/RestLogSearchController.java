package com.example.logsearch.controller;

import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.service.LogService;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.utils.ConfigProperties;
import com.example.logsearch.utils.validators.SearchInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/rest")
public class RestLogSearchController {

    private final LogService logService;
    private final ConfigProperties configProperties;
    private final SearchInfoValidator validator;

    @Autowired
    public RestLogSearchController(LogService logService,
                                   ConfigProperties configProperties,
                                   SearchInfoValidator validator) {
        this.logService = logService;
        this.configProperties = configProperties;
        this.validator = validator;
    }

    @PostMapping("/restLogSearch")
    public ResponseEntity<?> logSearch(@RequestBody SearchInfo searchInfo) {
        SearchInfoResult searchInfoResult = validator.validate(searchInfo);
        if (searchInfoResult != null) {
            return ResponseEntity.badRequest().body(searchInfoResult);
        }
        if(searchInfo.isRealization()) {
            logService.logSearch(searchInfo);
            String link = configProperties.getFileLink().replaceAll("\\\\", "/");
            return ResponseEntity.ok(Collections.singletonMap("link", link));
        }
        return ResponseEntity.ok(logService.logSearch(searchInfo));
    }
}
