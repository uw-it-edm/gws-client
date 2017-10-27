package edu.uw.edm.gws.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import edu.uw.edm.gws.GroupsWebServiceClient;
import edu.uw.edm.gws.impl.GroupsWebServiceClientImpl;

/**
 * @author Maxime Deravet Date: 10/24/17
 */
@Configuration
@ConditionalOnClass(GroupsWebServiceClient.class)
@AutoConfigureAfter(RestTemplateAutoConfiguration.class)
@EnableConfigurationProperties(GWSProperties.class)
public class GWSAutoConfiguration {
    private final GWSProperties gwsProperties;

    public GWSAutoConfiguration(GWSProperties gwsProperties) {
        this.gwsProperties = gwsProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupsWebServiceClient groupsWebServiceFacade(RestTemplate restTemplate) {

        return new GroupsWebServiceClientImpl(restTemplate, gwsProperties.getUrl());
    }
}
