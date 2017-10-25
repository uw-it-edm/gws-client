
package edu.uw.edm.gws.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.uw.edm.gws.GroupsWebServiceClient;
import edu.uw.edm.gws.model.GWSSearchType;
import edu.uw.edm.gws.model.GroupReference;
import edu.uw.edm.gws.model.GroupSearchResult;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GroupsWebServiceClientImpl implements GroupsWebServiceClient {


    private final RestTemplate restTemplate;

    private HttpHeaders headers;

    private final String gwsURL;
    private static String GWS_SEARCH_V2_URI = "group_sws/v2/search?type=%1$s&member=%2$s";

    public GroupsWebServiceClientImpl(RestTemplate restTemplate, String gwsURL) {
        this.restTemplate = restTemplate;
        this.gwsURL = gwsURL;

        ArrayList<MediaType> alist = new ArrayList<>();
        alist.add(MediaType.TEXT_XML);
        headers = new HttpHeaders();
        headers.setAccept(alist);
        log.info("Group Web Service headers: " + headers.toString());
    }


    @Override
    public List<GroupReference> getGroupsForUser(String memberId, GWSSearchType searchType) {
        return this.getGroupsForUser(memberId, searchType, null);
    }

    @Override
    public List<GroupReference> getGroupsForUser(String memberId, GWSSearchType searchType, String groupPrefix) {
        if (log.isDebugEnabled()) {
            log.debug("getGrantedAuthorities(\"" + memberId + "\")");
        }

        if (StringUtils.isEmpty(memberId)) {
            log.debug("getGrantedAuthorities(\"" + memberId + "\"): memberId is null or empty");
            return Collections.emptyList();
        }
        ResponseEntity<GroupSearchResult> result = searchGWS(searchType, memberId);

        if (result == null) {
            log.debug("getGrantedAuthorities(\"" + memberId + "\") group service returned null.");
            return Collections.emptyList();
        }
        GroupSearchResult body = result.getBody();
        if (body == null) {
            log.debug("getGrantedAuthorities(\"" + memberId + "\") group service returned null body.");
            return Collections.emptyList();
        }
        List<GroupReference> grs = body.getGroupreference();
        if (grs == null || grs.size() == 0) {
            log.debug("getGrantedAuthorities(\"" + memberId + "\") group service returned no groups.");
            return Collections.emptyList();
        }


        if (StringUtils.isEmpty(groupPrefix)) {
            return grs;
        } else {
            return grs
                    .stream()
                    .filter(group -> group.getName().startsWith(groupPrefix))
                    .collect(Collectors.toList());
        }

    }

    private ResponseEntity<GroupSearchResult> searchGWS(GWSSearchType searchType, String memberId) {
        HttpEntity request = new HttpEntity(headers);
        String url = getSearchURL(searchType, memberId);
        log.debug("search URL: " + url);
        return restTemplate.exchange(url, HttpMethod.GET, request, GroupSearchResult.class);
    }

    protected String getSearchURL(GWSSearchType type, String memberId) {
        return gwsURL + String.format(GWS_SEARCH_V2_URI, type.name(), memberId);
    }


}
