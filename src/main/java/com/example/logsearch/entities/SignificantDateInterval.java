package com.example.logsearch.entities;

import com.example.logsearch.utils.formatters.LocalDateTimeAdapter;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignificantDateInterval", propOrder = {
        "dateFrom",
        "dateTo"
})
@Getter
@Setter
@XmlRootElement(name = "significantDateInterval")
public class SignificantDateInterval implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    private LocalDateTime dateFrom;

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    private LocalDateTime dateTo;
}
