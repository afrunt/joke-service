package com.afrunt.stupidjokes.jokeservice.crawler.jokedataset;

import org.springframework.stereotype.Component;

/**
 * @author Andrii Frunt
 */
@Component
public class RedditJokesItemReader extends AbstractJokeDatasetItemReader {
    public RedditJokesItemReader() {
        super("reddit", "https://raw.githubusercontent.com/taivop/joke-dataset/master/reddit_jokes.json");
    }
}
