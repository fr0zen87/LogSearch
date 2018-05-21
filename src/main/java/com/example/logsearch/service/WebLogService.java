package com.example.logsearch.service;

import com.example.logsearch.entities.Search;
import com.example.logsearch.entities.SearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebLogService {

    private final Search search;

    @Autowired
    public WebLogService(Search search) {
        this.search = search;
    }

    public String logSearch(SearchInfo searchInfo) {
        return null;
    }
}
