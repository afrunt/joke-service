package com.afrunt.stupidjokes.jokeservice.api;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrii Frunt
 */
public interface JokeService {
    Optional<Joke> random();

    long count();

    Optional<Joke> findById(long id);

    void create(Collection<String> jokes);

    default void create(String joke) {
        create(List.of(joke));
    }
}
