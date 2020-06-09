package com.es.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@Slf4j
public class ElasticSearchConfiguration {


    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private String elasticsearchPort;

    RestHighLevelClient client;

    @Bean
    public RestHighLevelClient client() {
/*      use in case where ES is hosted on aws or docker
        RestClientBuilder builder
                = RestClient.builder(new HttpHost(elasticsearchHost));*/

        RestClientBuilder builder = RestClient.builder(new HttpHost(elasticsearchHost, Integer.parseInt(elasticsearchPort)));
        client = new RestHighLevelClient(builder);
        return client;

    }

    @PreDestroy
    public void closeEsClient() {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

