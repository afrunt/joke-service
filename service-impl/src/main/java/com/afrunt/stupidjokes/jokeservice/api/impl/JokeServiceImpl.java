package com.afrunt.stupidjokes.jokeservice.api.impl;

import com.afrunt.stupidjokes.jokeservice.api.Joke;
import com.afrunt.stupidjokes.jokeservice.api.JokeService;
import com.afrunt.stupidjokes.jokeservice.api.impl.entity.JokeEntity;
import com.afrunt.stupidjokes.jokeservice.api.impl.repository.JokeRepository;
import com.afrunt.stupidjokes.jokeservice.commons.Chunks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
@Service
public class JokeServiceImpl implements JokeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JokeServiceImpl.class);

    private static final Function<JokeEntity, Joke> ENTITY_TO_JOKE = jokeEntity -> new Joke()
            .setId(jokeEntity.getId())
            .setBody(jokeEntity.getBody());

    private static final Function<String, JokeEntity> STRING_TO_ENTITY = body -> new JokeEntity()
            .setBody(body)
            .setHash(body.hashCode());

    private JokeRepository jokeRepository;

    @Autowired
    public JokeServiceImpl(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    @Override
    public Optional<Joke> random() {
        StopWatch sw = new StopWatch();
        sw.start();
        Optional<Joke> optionalJoke = jokeRepository.findRandomJokes(PageRequest.of(0, 1))
                .stream()
                .findAny()
                .map(ENTITY_TO_JOKE);
        sw.stop();
        LOGGER.info("Random joke retrieved in {}ms", sw.getTotalTimeMillis());
        return optionalJoke;
    }

    @Override
    @Transactional
    public void drop() {
        jokeRepository.drop();
    }

    @Override
    @Transactional
    public void dropDuplicates() {
        Set<Long> ids = jokeRepository.findIdsOfDuplicatesToRemove();
        if (!ids.isEmpty()) {
            StopWatch sw = new StopWatch();
            sw.start();


            Chunks.split(ids)
                    .forEach(chunk -> jokeRepository.deleteByIdIn(chunk));
            sw.stop();
            LOGGER.info("Delete {} duplicates by id. {}ms", ids.size(), sw.getTotalTimeMillis());
        } else {
            LOGGER.info("No duplicates found");
        }
    }

    @Override
    public long count() {
        return jokeRepository.count();
    }

    @Override
    public Optional<Joke> findById(long id) {
        StopWatch sw = new StopWatch();
        sw.start();
        Optional<Joke> joke = jokeRepository
                .findById(id)
                .map(ENTITY_TO_JOKE);
        sw.stop();
        LOGGER.info("Joke {} retrieved in {}ms", id, sw.getTotalTimeMillis());
        return joke;
    }

    @Override
    @Transactional
    public void create(Collection<String> jokes) {
        List<List<String>> chunks = Chunks.split(jokes);
        LOGGER.info("{} unique jokes. {} chunks", jokes.size(), chunks.size());

        Consumer<List<String>> chunkConsumer = chunk -> {
            Set<Integer> existingHashes = jokeRepository.findExistingHashes(chunk.stream()
                    .filter(Objects::nonNull)
                    .map(String::hashCode)
                    .collect(Collectors.toSet())
            );

            LOGGER.info("{} hashes found", existingHashes.size());
            List<JokeEntity> jokeEntities = chunk.stream()
                    .filter(Objects::nonNull)
                    .filter(body -> !body.equals(""))
                    .filter(body -> body.length() < 50000)
                    .filter(body -> !existingHashes.contains(body.hashCode()))
                    .map(STRING_TO_ENTITY)
                    .collect(Collectors.toList());

            LOGGER.info("{} joke to save", jokeEntities.size());

            jokeRepository.saveAll(jokeEntities);
        };

        chunks.forEach(chunkConsumer);
    }
}
