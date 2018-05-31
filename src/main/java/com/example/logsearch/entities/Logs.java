package com.example.logsearch.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Logs", propOrder = {
        "creator",
        "searchInfo",
        "searchInfoResult",
        "application"
})
@XmlRootElement(name = "logs")
public class Logs {

    protected String creator;

    protected SearchInfo searchInfo;

    protected SearchInfoResult searchInfoResult;

    protected String application;

    public Logs() {
    }

    public Logs(String creator, SearchInfo searchInfo, SearchInfoResult searchInfoResult, String application) {
        this.creator = creator;
        this.searchInfo = searchInfo;
        this.searchInfoResult = searchInfoResult;
        this.application = application;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public SearchInfo getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    public SearchInfoResult getSearchInfoResult() {
        return searchInfoResult;
    }

    public void setSearchInfoResult(SearchInfoResult searchInfoResult) {
        this.searchInfoResult = searchInfoResult;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
