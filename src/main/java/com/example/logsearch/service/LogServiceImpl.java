package com.example.logsearch.service;

import com.example.logsearch.utils.FileGenerate;
import com.example.logsearch.utils.FileSearch;
import com.example.logsearch.utils.LogsSearch;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    private final LogsSearch search;
    private final FileSearch fileSearch;
    private final FileGenerate fileGenerate;

    @Autowired
    public LogServiceImpl(LogsSearch search, FileSearch fileSearch, FileGenerate fileGenerate) {
        this.search = search;
        this.fileSearch = fileSearch;
        this.fileGenerate = fileGenerate;
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
        return fileSearch.fileSearch(searchInfo);
    }

    @Override
    public void fileGenerate(SearchInfo searchInfo) {
        fileGenerate.fileGenerate(searchInfo);
    }

//    @Async
//    public CompletableFuture<Boolean> fileSearchh(SearchInfo searchInfo) {
//        return CompletableFuture.completedFuture(search.fileSearch(searchInfo));
//    }
//
//    @Async
//    public CompletableFuture<String> fileGeneratee(SearchInfo searchInfo) {
//        search.fileGenerate(searchInfo);
//        return null;
//    }
}
