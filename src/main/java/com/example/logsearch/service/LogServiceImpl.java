package com.example.logsearch.service;

import com.example.logsearch.utils.FileGenerate;
import com.example.logsearch.utils.FileSearch;
import com.example.logsearch.utils.LogsSearch;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LogServiceImpl implements LogService {

    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

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
        logger.info("Log service incoming data: " + searchInfo);
        if (searchInfo.isRealization()) {
            boolean isFileFound = fileSearch(searchInfo);
            if (!isFileFound) {
                File file = fileGenerate.generateUniqueFile(searchInfo.getFileExtension());
                fileGenerate(searchInfo, file);
                logger.info("Generated result file: " + file.toString());
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
    @Async
    public void fileGenerate(SearchInfo searchInfo, File file) {
        fileGenerate.fileGenerate(searchInfo, file);
    }
}
