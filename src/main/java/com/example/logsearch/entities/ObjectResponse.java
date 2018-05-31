package com.example.logsearch.entities;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
@Getter
@Setter
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
}
