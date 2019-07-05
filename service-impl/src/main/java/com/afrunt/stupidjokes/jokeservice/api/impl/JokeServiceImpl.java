package com.afrunt.stupidjokes.jokeservice.api.impl;

import com.afrunt.stupidjokes.jokeservice.api.Joke;
import com.afrunt.stupidjokes.jokeservice.api.JokeService;
import com.afrunt.stupidjokes.jokeservice.api.impl.entity.JokeEntity;
import com.afrunt.stupidjokes.jokeservice.api.impl.repository.JokeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

    private JokeRepository jokeRepository;

    @Autowired
    public JokeServiceImpl(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    @Override
    public Optional<Joke> random() {
        long count = jokeRepository.count();
        if (count == 0) {
            return Optional.empty();
        }

        long index = Math.abs(ThreadLocalRandom.current().nextLong()) % count;

        return jokeRepository.findAll(PageRequest.of((int) index, 1))
                .stream()
                .findAny()
                .map(ENTITY_TO_JOKE);
    }

    @Override
    public long count() {
        return jokeRepository.count();
    }

    @Override
    public Optional<Joke> findById(long id) {
        return jokeRepository.findById(id)
                .map(ENTITY_TO_JOKE);
    }

    @Override
    @Transactional
    public void create(Collection<String> jokes) {
        jokes = new HashSet<>(jokes);

        LOGGER.info("{} unique jokes", jokes.size());

        Set<Integer> existingHashes = jokeRepository.findExistingHashes(jokes.stream()
                .filter(Objects::nonNull)
                .map(String::hashCode)
                .collect(Collectors.toSet())
        );

        LOGGER.info("{} hashes found", existingHashes.size());

        List<JokeEntity> jokeEntities = jokes.stream()
                .filter(Objects::nonNull)
                .filter(body -> body.length() < 50000)
                .filter(body -> !existingHashes.contains(body.hashCode()))
                .map(body -> new JokeEntity()
                        .setBody(body)
                        .setHash(body.hashCode())
                )
                .collect(Collectors.toList());

        LOGGER.info("{} joke to save", jokeEntities.size());

        jokeRepository.saveAll(jokeEntities);
    }
}
