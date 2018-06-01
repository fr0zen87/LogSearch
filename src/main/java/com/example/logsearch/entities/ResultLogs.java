package com.example.logsearch.entities;

import com.example.logsearch.utils.formatters.LocalDateTimeAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultLogs", propOrder = {
        "timeMoment",
        "fileName",
        "content"
})
@Getter
@Setter
@ToString
public class ResultLogs {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    private LocalDateTime timeMoment;

    @XmlElement(required = true)
    private String fileName;

    @XmlElement(required = true)
    private String content;
}
