package com.example.logsearch.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FileExtension")
@XmlEnum
public enum FileExtension {

    PDF,
    RTF,
    HTML,
    XML,
    LOG,
    DOC;

    public String value() {
        return name();
    }
}
