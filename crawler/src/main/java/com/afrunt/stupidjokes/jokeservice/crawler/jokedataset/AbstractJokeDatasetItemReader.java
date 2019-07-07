package com.afrunt.stupidjokes.jokeservice.crawler.jokedataset;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

/**
 * @author Andrii Frunt
 */
public abstract class AbstractJokeDatasetItemReader extends AbstractItemStreamItemReader<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJokeDatasetItemReader.class);
    private String datasetName;
    private String url;
    private JsonParser parser;
    private boolean started = false;
    private int jokeCount = 0;

    public AbstractJokeDatasetItemReader(String datasetName, String url) {
        this.datasetName = datasetName;
        this.url = url;
    }

    @PostConstruct
    public void init() throws IOException {
        JsonFactory factory = new JsonFactory();
        parser = factory.createParser(new URL(url));
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!started) {
            started = true;
            parser.nextToken();
        }

        if (parser.currentToken() == JsonToken.END_ARRAY) {
            LOGGER.info("{} jokes fetched from {} dataset", jokeCount, datasetName);
            return null;
        } else if (parser.currentToken() == JsonToken.START_ARRAY) {
            parser.nextToken();
        }

        String joke = readJoke(parser);

        ++jokeCount;

        if (jokeCount % 10000 == 0) {
            LOGGER.info("{} jokes fetched from {} dataset", jokeCount, datasetName);
        }

        return joke;
    }

    protected String readJoke(JsonParser parser) throws IOException {
        String title = "";
        String body = "";
        JsonToken token = parser.getCurrentToken();

        while (token != JsonToken.END_OBJECT) {
            token = parser.nextToken();

            if (token == JsonToken.FIELD_NAME && "title".equals(parser.getValueAsString())) {
                token = parser.nextToken();
                title = parser.getValueAsString();
            }

            if (token == JsonToken.FIELD_NAME && "body".equals(parser.getValueAsString())) {
                token = parser.nextToken();
                body = parser.getValueAsString();
            }

        }
        parser.nextToken();
        if ("".equals(title)) {
            return body;
        } else {
            return title + "\n" + body;
        }
    }
}
