package edu.uw.edm.gws;

import java.util.List;

import edu.uw.edm.gws.model.GWSSearchType;
import edu.uw.edm.gws.model.GroupReference;

/**
 * @author Maxime Deravet
 */
public interface GroupsWebServiceClient {

    /**
     * Calls the UW Group Service for integration. returns an array of granted authorities (e.g.
     * ["ROLE_U_IT", "ROLE_U_IT_OP"])
     *
     * @param memberId an Id  that uniquely identifies a member in a group.
     * @return a list of edu.uw.edm.gws.model.GroupReference, empty otherwise.
     */
    List<GroupReference> getGroupsForUser(String memberId, GWSSearchType searchType);


    /**
     * Calls the UW Group Service for integration. returns an array of granted authorities (e.g.
     * ["ROLE_U_IT", "ROLE_U_IT_OP"])
     *
     * @param memberId    an Id  that uniquely identifies a member in a group.
     * @param groupPrefix prefix of the groups you want to be returned.
     * @return a list of edu.uw.edm.gws.model.GroupReference that matches the `groupPrefix`, empty
     * otherwise.
     */
    List<GroupReference> getGroupsForUser(String memberId, GWSSearchType searchType, String groupPrefix);


}
