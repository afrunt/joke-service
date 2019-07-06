package com.afrunt.stupidjokes.jokeservice.api.impl.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andrii Frunt
 */

public abstract class Chunks {

    private Chunks() {
    }

    public static <T> List<List<T>> split(Collection<T> src, Integer chunkSize) {
        if (src.size() <= chunkSize) {
            return List.of(new ArrayList<>(src));
        }

        List<T> chunk = new ArrayList<>();
        List<List<T>> result = new ArrayList<>();

        Iterator<T> iterator = src.iterator();

        for (int i = 0; i < src.size(); i++) {
            T element = iterator.next();
            chunk.add(element);
            if ((i + 1) % chunkSize == 0) {
                result.add(chunk);
                chunk = new ArrayList<>();
            }
        }

        if (!chunk.isEmpty()) {
            result.add(chunk);
        }

        return result;
    }
}
