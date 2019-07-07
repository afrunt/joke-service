package com.afrunt.stupidjokes.jokeservice.crawler.randomjokecrawler;

import com.afrunt.randomjoke.Jokes;
import com.afrunt.stupidjokes.jokeservice.crawler.GenericJobFactory;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Andrii Frunt
 */
@Profile({"full", "randomjokecrawler"})
@Configuration
public class RandomJokeCrawlerJobConfiguration {
    @Autowired
    private GenericJobFactory genericJobFactory;

    @Bean
    public Jokes jokes() {
        return new Jokes()
                .withDefaultSuppliers();
    }

    @Bean
    public RandomJokeCrawlerStreamReader jokeStreamReader() {
        return new RandomJokeCrawlerStreamReader();
    }

    @Bean
    public Job crawlJokesJob(RandomJokeCrawlerStreamReader reader) {
        return genericJobFactory.create("randomJokeCrawler", reader);
    }
}
