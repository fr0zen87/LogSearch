package com.example.logsearch.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchInfo", propOrder = {
        "regularExpression",
        "dateIntervals",
        "location",
        "realization",
        "fileExtension"
})
@XmlRootElement(name = "searchInfo")
@Getter
@Setter
@ToString
public class SearchInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(required = true)
    private String regularExpression;

    @XmlElement(required = true)
    private List<SignificantDateInterval> dateIntervals = new ArrayList<>();

    private String location;

    @XmlElement(required = true)
    private boolean realization;

    @XmlSchemaType(name = "string")
    private FileExtension fileExtension;
}
