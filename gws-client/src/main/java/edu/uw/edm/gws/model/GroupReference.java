package edu.uw.edm.gws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Maxime Deravet Date: 10/24/17
 */

@XmlRootElement(name = "groupreference")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "GroupReferenceType", namespace = "http://webservices.washington.edu/gws/")
public class GroupReference {
    @XmlElement(name="regid")
    private String regid;
    public String getRegid() { return regid; }

    @XmlElement(name="tittle")
    private String title;
    public String getTitle() { return title; }

    @XmlElement(name="description")
    private String description;
    public String getDescription() { return description; }

    @XmlElement(name="name")
    private String name;
    public String getName() { return name; }
}