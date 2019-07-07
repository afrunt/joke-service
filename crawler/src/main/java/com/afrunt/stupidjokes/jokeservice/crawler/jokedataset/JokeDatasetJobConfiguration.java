package com.afrunt.stupidjokes.jokeservice.crawler.jokedataset;

import com.afrunt.stupidjokes.jokeservice.crawler.GenericJobFactory;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Andrii Frunt
 */
@Profile({"full", "jokedataset"})
@Configuration
public class JokeDatasetJobConfiguration {
    @Autowired
    private GenericJobFactory genericJobFactory;

    @Bean
    public Job crawlRedditJokesJob(RedditJokesItemReader reader) {
        return genericJobFactory.create("Reddit", reader);
    }

    @Bean
    public Job crawlStupidStuffJokesJob(StupidStuffJokesItemReader reader) {
        return genericJobFactory.create("StupidStuff", reader);
    }

    @Bean
    public Job crawlWockaJokesJob(WockaJokesItemReader reader) {
        return genericJobFactory.create("Wocka", reader);
    }
}
