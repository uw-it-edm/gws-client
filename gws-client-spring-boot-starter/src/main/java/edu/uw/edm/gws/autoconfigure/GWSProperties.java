package edu.uw.edm.gws.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Maxime Deravet Date: 10/24/17
 */
@ConfigurationProperties(prefix = "gws")
@Getter
@Setter
public class GWSProperties {
    /**
     * Group webservices URL
     */
    private String url = "https://iam-ws.u.washington.edu:7443/";
}
