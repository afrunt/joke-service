package com.afrunt.stupidjokes.jokeservice.crawler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Andrii Frunt
 */
@EnableBatchProcessing
@SpringBootApplication
public class Crawler {
    public static void main(String[] args) {
        SpringApplication.run(Crawler.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
