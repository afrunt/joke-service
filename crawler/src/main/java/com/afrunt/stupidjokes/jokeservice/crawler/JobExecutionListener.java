package com.afrunt.stupidjokes.jokeservice.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Andrii Frunt
 */
@Component
public class JobExecutionListener extends JobExecutionListenerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutionListener.class);
    private RestTemplate restTemplate;
    private String jokeServiceUrl;
    private CrawlerJokeEntityRepository crawlerJokeEntityRepository;

    @Autowired
    public JobExecutionListener(CrawlerJokeEntityRepository crawlerJokeEntityRepository, RestTemplate restTemplate, @Value("${jokeservice.url}") String jokeServiceUrl) {
        this.restTemplate = restTemplate;
        this.jokeServiceUrl = jokeServiceUrl;
        this.crawlerJokeEntityRepository = crawlerJokeEntityRepository;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long jokesInInternalDb = crawlerJokeEntityRepository.count();
        crawlerJokeEntityRepository.drop();
        LOGGER.info("{} jokes dropped from internal DB", jokesInInternalDb);
        restTemplate.delete(jokeServiceUrl + "/api/joke/drop-duplicates");
        LOGGER.info("Duplicates dropped");
        LOGGER.info("Crawler Batch Job completed");
    }
}
