package com.afrunt.stupidjokes.jokeservice.crawler.jokedataset;

import org.springframework.stereotype.Component;

/**
 * @author Andrii Frunt
 */
@Component
public class WockaJokesItemReader extends AbstractJokeDatasetItemReader {
    public WockaJokesItemReader() {
        super("wocka", "https://raw.githubusercontent.com/taivop/joke-dataset/master/wocka.json");
    }
}
