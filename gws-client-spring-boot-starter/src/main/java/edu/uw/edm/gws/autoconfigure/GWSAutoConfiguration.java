package edu.uw.edm.gws.autoconfigure;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.function.Supplier;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import edu.uw.edm.gws.GroupsWebServiceClient;
import edu.uw.edm.gws.autoconfigure.security.KeyManagerCabinet;
import edu.uw.edm.gws.impl.GroupsWebServiceClientImpl;

/**
 * @author Maxime Deravet Date: 10/24/17
 */
@Configuration
@ConditionalOnClass(value = {GroupsWebServiceClient.class, RestTemplateBuilder.class,})
@AutoConfigureAfter(RestTemplateAutoConfiguration.class)
@EnableConfigurationProperties(GWSProperties.class)
public class GWSAutoConfiguration {
    private final GWSProperties gwsProperties;

    public GWSAutoConfiguration(GWSProperties gwsProperties) {
        this.gwsProperties = gwsProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupsWebServiceClient groupsWebServiceFacade(@Qualifier("gws-client") RestTemplate restTemplate) {

        return new GroupsWebServiceClientImpl(restTemplate, gwsProperties.getUrl());
    }

    private CloseableHttpClient httpClient(final PoolingHttpClientConnectionManager connectionManager) throws NoSuchAlgorithmException, KeyManagementException {

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setExpectContinueEnabled(true);

        return HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfigBuilder.build()).build();
    }

    private PoolingHttpClientConnectionManager connectionManager(final KeyManagerCabinet cabinet) throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustManagers = cabinet.getTrustManagers();


        SSLContext context = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
        context.init(cabinet.getKeyManagers(), trustManagers, new SecureRandom());

        HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();

        ConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, hostnameVerifier);

        final Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", socketFactory)
                .build();

        return new PoolingHttpClientConnectionManager(sfr);
    }

    @Bean
    @Qualifier("gws-client")
    public KeyManagerCabinet keyManagerCabinet(GWSProperties gwsProperties) throws Exception {
        return new KeyManagerCabinet.Builder(gwsProperties.getKeystoreLocation(), gwsProperties.getKeystorePassword()).build();
    }


    @Bean
    @Qualifier("gws-client")
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(@Qualifier("gws-client") KeyManagerCabinet keyManagerCabinet) throws NoSuchAlgorithmException, KeyManagementException {

        final PoolingHttpClientConnectionManager connectionManager = connectionManager(keyManagerCabinet);

        final HttpClient httpClient = httpClient(connectionManager);
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    @Qualifier("gws-client")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, @Qualifier("gws-client") HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) throws NoSuchAlgorithmException, KeyManagementException {
        return restTemplateBuilder.requestFactory(new Supplier<ClientHttpRequestFactory>() {
            @Override
            public ClientHttpRequestFactory get() {
                return httpComponentsClientHttpRequestFactory;
            }
        }).build();
    }
}
