package edu.uw.edm.gws.impl;

import org.junit.Before;
import org.junit.Test;

import edu.uw.edm.gws.model.GWSSearchType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Maxime Deravet Date: 10/25/17
 */
public class GroupsWebServiceClientImplTest {

    private GroupsWebServiceClientImpl gws;

    @Before
    public void setUp() {
        gws = new GroupsWebServiceClientImpl(null, "http://gws.com/");
    }

    @Test
    public void searchUrlTest() {
        String searchURL = gws.getSearchURL(GWSSearchType.direct, "toto");

        assertThat(searchURL, is(equalTo("http://gws.com/group_sws/v2/search?type=direct&member=toto")));
    }
}