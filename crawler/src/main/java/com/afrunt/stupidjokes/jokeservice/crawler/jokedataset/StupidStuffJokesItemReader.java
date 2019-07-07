package com.afrunt.stupidjokes.jokeservice.crawler.jokedataset;

import org.springframework.stereotype.Component;

/**
 * @author Andrii Frunt
 */
@Component
public class StupidStuffJokesItemReader extends AbstractJokeDatasetItemReader {
    public StupidStuffJokesItemReader() {
        super("stupidstuff", "https://raw.githubusercontent.com/taivop/joke-dataset/master/stupidstuff.json");
    }
}
