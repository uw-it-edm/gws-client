package edu.uw.edm.gws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Maxime Deravet Date: 10/24/17
 */
@XmlRootElement(name = "gws")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "PageType", namespace = "http://webservices.washington.edu/groups/")
public class GroupSearchResult {
    @XmlElementWrapper(name = "groupreferences")
    private List<GroupReference> groupreference; // the variable name "groupreference" must be the same as the element name return from group service

    public List<GroupReference> getGroupreference() {
        return groupreference;
    }
}
