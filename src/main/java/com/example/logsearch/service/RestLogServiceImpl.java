package com.example.logsearch.service;

import com.example.logsearch.entities.Search;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestLogServiceImpl implements LogService {

    private final Search search;

    @Autowired
    public RestLogServiceImpl(Search search) {
        this.search = search;
    }

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        return search.logSearch(searchInfo);
    }
}
