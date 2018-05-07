package com.example.logsearch.Service;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;

public interface LogService {

    SearchInfoResult logSearch(SearchInfo searchInfo);
}
