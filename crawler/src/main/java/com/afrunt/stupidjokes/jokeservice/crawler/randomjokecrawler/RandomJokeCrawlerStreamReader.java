package com.afrunt.stupidjokes.jokeservice.crawler.randomjokecrawler;

import com.afrunt.randomjoke.Joke;
import com.afrunt.randomjoke.Jokes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andrii Frunt
 */
public class RandomJokeCrawlerStreamReader extends AbstractItemStreamItemReader<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomJokeCrawlerStreamReader.class);

    private volatile int i = 0;

    private Queue<Joke> jokesQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    private Jokes jokes;

    @Value("${crawler.jobs.randomjokecrawler.batchSize:1000}")
    private int batchSize;

    @Value("${crawler.jobs.randomjokecrawler.bufferSize:50}")
    private int bufferSize;

    @Override
    public String read() {
        if (finished()) {
            return null;
        }

        if (bufferIsEmpty()) {
            loadBuffer();
        }

        return jokeFromBuffer().getText();
    }

    private synchronized boolean finished() {
        i = i + 1;
        return i == batchSize;
    }

    private synchronized void loadBuffer() {
        StopWatch sw = new StopWatch();
        sw.start();
        List<Joke> jokes = this.jokes.randomJokes(bufferSize, 10);
        sw.stop();
        jokesQueue.addAll(jokes);
        LOGGER.info("{} jokes loaded to buffer. {}ms", jokes.size(), sw.getTotalTimeMillis());
    }

    private synchronized boolean bufferIsEmpty() {
        return jokesQueue.isEmpty();
    }

    private synchronized Joke jokeFromBuffer() {
        return jokesQueue.poll();
    }
}
