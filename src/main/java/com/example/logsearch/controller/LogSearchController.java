package com.example.logsearch.controller;

import com.example.logsearch.Service.LogService;
import com.example.logsearch.Service.RestLogServiceImpl;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogSearchController {

    private final LogService logService;

    @Autowired
    public LogSearchController(RestLogServiceImpl logService) {
        this.logService = logService;
    }

    @PostMapping("/logSearch")
    SearchInfoResult logSearch(@RequestBody SearchInfo searchInfo) {
        return logService.logSearch(searchInfo);
    }
}
