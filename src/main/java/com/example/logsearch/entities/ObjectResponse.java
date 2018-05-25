package com.example.logsearch.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class ObjectResponse {

    protected String link;

    protected SearchInfoResult searchInfoResult;

    public ObjectResponse() {
    }

    public ObjectResponse(String link) {
        this.link = link;
    }

    public ObjectResponse(SearchInfoResult searchInfoResult) {
        this.searchInfoResult = searchInfoResult;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public SearchInfoResult getSearchInfoResult() {
        return searchInfoResult;
    }

    public void setSearchInfoResult(SearchInfoResult searchInfoResult) {
        this.searchInfoResult = searchInfoResult;
    }
}
