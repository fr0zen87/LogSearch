package com.example.logsearch.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchInfoResult", propOrder = {
        "emptyResultMessage",
        "errorCode",
        "errorMessage",
        "resultLogs"
})
@XmlRootElement(name = "searchInfoResult")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SearchInfoResult {

    private String emptyResultMessage;

    private Long errorCode;

    private String errorMessage;

    private List<ResultLogs> resultLogs = new ArrayList<>();

    public SearchInfoResult() {
    }

    public SearchInfoResult(Long errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
