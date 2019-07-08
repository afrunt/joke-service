package com.afrunt.stupidjokes.jokeservice.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
@EnableBatchProcessing
@SpringBootApplication
public class Crawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);

    public static void main(String[] args) {
        LOGGER.info("ssl disabled");
        SpringApplication.run(Crawler.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "hostsOverrides")
    public Map<String, String> hostsOverrides() throws IOException {
        Path path = Paths.get("/deployment/config/hosts.overrides.properties");
        File file = path.toFile();
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return Collections.emptyMap();
        }

        Properties props = new Properties();
        props.load(new FileInputStream(file));
        
        LOGGER.info("Hosts overrides found");

        return props.entrySet().stream()
                .peek(e -> LOGGER.info("{} -> {}", e.getKey(), e.getValue()))
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
    }
}
