package com.afrunt.stupidjokes.jokeservice.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
@Component
public class CrawlerJokeEntityRESTWriter extends AbstractItemStreamItemWriter<CrawlerJokeEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerJokeEntityRESTWriter.class);
    private RestTemplate restTemplate;
    private String jokeServiceUrl;

    @Autowired
    public CrawlerJokeEntityRESTWriter(RestTemplate restTemplate, @Value("${jokeservice.url}") String jokeServiceUrl) {
        this.restTemplate = restTemplate;
        this.jokeServiceUrl = jokeServiceUrl;
    }

    @Override
    public void write(List<? extends CrawlerJokeEntity> items) throws Exception {
        Set<String> jokes = items.stream().map(CrawlerJokeEntity::getBody).collect(Collectors.toSet());
        LOGGER.info("Submit {} jokes to Joke Service", jokes.size());
        restTemplate.postForEntity(jokeServiceUrl + "/api/joke/bulk", jokes, Void.class);
    }
}
