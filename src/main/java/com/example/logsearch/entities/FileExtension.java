//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.08 at 05:48:42 PM MSK 
//


package com.example.logsearch.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FileExtension.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FileExtension"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PDF"/&gt;
 *     &lt;enumeration value="RTF"/&gt;
 *     &lt;enumeration value="HTML"/&gt;
 *     &lt;enumeration value="XML"/&gt;
 *     &lt;enumeration value="LOG"/&gt;
 *     &lt;enumeration value="DOC"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
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

    public static FileExtension fromValue(String v) {
        return valueOf(v);
    }

}
