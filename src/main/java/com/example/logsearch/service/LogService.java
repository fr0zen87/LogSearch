package com.example.logsearch.service;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;

public interface LogService {

    SearchInfoResult logSearch(SearchInfo searchInfo);

    boolean fileSearch(SearchInfo searchInfo);

    void fileGenerate(SearchInfo searchInfo);
}
