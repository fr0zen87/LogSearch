package com.example.logsearch.entities;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class Logs {

    private String creator;

    private SearchInfo searchInfo;

    private SearchInfoResult searchInfoResult;

    private String application;

    public Logs() {
    }

    public Logs(String creator, SearchInfo searchInfo, SearchInfoResult searchInfoResult, String application) {
        this.creator = creator;
        this.searchInfo = searchInfo;
        this.searchInfoResult = searchInfoResult;
        this.application = application;
    }
}
