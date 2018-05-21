package com.example.logsearch.controller;

import com.example.logsearch.service.LogService;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.utils.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    public RestLogSearchController(@Qualifier("logServiceImpl") LogService logService, ConfigProperties configProperties) {
        this.logService = logService;
        this.configProperties = configProperties;
    }

    @PostMapping("/restLogSearch")
    ResponseEntity<?> logSearch(@RequestBody SearchInfo searchInfo) {
        if(searchInfo.isRealization()) {
            logService.logSearch(searchInfo);
            String link = configProperties.getFileLink().replaceAll("\\\\", "/");
            return ResponseEntity.ok(Collections.singletonMap("link", link));
        }
        return ResponseEntity.ok(logService.logSearch(searchInfo));
    }
}
