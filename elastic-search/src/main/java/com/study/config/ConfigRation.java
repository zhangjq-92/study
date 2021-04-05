package com.study.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ConfigRation {

    @Autowired
    private ElasticSearchConfigurationProperties configurationProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        log.info("elastic search configuration properties {}", configurationProperties);
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(configurationProperties.getHost(), configurationProperties.getPort(), configurationProperties.getScheme())));
        return client;
    }
}
