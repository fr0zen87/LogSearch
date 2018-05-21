package com.example.logsearch.service;

import com.example.logsearch.entities.Search;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    private final Search search;

    @Autowired
    public LogServiceImpl(Search search) {
        this.search = search;
    }

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
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
