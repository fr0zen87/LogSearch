//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.08 at 05:48:42 PM MSK 
//


package com.example.logsearch.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for SearchInfo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SearchInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="regularExpression" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dateIntervals" type="{http://entities.logsearch.example.com}SignificantDateInterval" maxOccurs="unbounded"/&gt;
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="realization" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="fileExtension" type="{http://entities.logsearch.example.com}FileExtension" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchInfo", propOrder = {
    "regularExpression",
    "dateIntervals",
    "location",
    "realization",
    "fileExtension"
})
@XmlRootElement(name = "searchInfo")
public class SearchInfo implements Serializable {

    @XmlElement(required = true)
    protected String regularExpression;
    @XmlElement(required = true)
    protected List<SignificantDateInterval> dateIntervals;
    protected String location;
    protected boolean realization;
    @XmlSchemaType(name = "string")
    protected FileExtension fileExtension;

    /**
     * Gets the value of the regularExpression property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRegularExpression() {
        return regularExpression;
    }

    /**
     * Sets the value of the regularExpression property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRegularExpression(String value) {
        this.regularExpression = value;
    }

    /**
     * Gets the value of the dateIntervals property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateIntervals property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateIntervals().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignificantDateInterval }
     *
     *
     */
    public List<SignificantDateInterval> getDateIntervals() {
        if (dateIntervals == null) {
            dateIntervals = new ArrayList<SignificantDateInterval>();
        }
        return this.dateIntervals;
    }

    /**
     * Gets the value of the location property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the realization property.
     *
     */
    public boolean isRealization() {
        return realization;
    }

    /**
     * Sets the value of the realization property.
     *
     */
    public void setRealization(boolean value) {
        this.realization = value;
    }

    /**
     * Gets the value of the fileExtension property.
     *
     * @return
     *     possible object is
     *     {@link FileExtension }
     *
     */
    public FileExtension getFileExtension() {
        return fileExtension;
    }

    /**
     * Sets the value of the fileExtension property.
     *
     * @param value
     *     allowed object is
     *     {@link FileExtension }
     *
     */
    public void setFileExtension(FileExtension value) {
        this.fileExtension = value;
    }

}
