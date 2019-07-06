package com.afrunt.stupidjokes.jokeservice.api.impl.repository;

import com.afrunt.stupidjokes.jokeservice.api.impl.entity.JokeEntity;
import com.afrunt.stupidjokes.jokeservice.api.impl.misc.Chunks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
@Repository
public interface JokeRepository extends JpaRepository<JokeEntity, Long> {
    Logger LOGGER = LoggerFactory.getLogger(JokeRepository.class);

    @Query("SELECT j.hash FROM JokeEntity j WHERE j.hash in :hashes")
    Set<Integer> findExistingHashes(@Param("hashes") Collection<Integer> hashes);

    @Modifying
    @Query("DELETE FROM JokeEntity")
    void drop();

    @Query("SELECT je.hash FROM JokeEntity je GROUP BY je.hash HAVING count(je) > 1")
    Set<Integer> findDuplicatedHashes();

    @Query("SELECT je.id FROM JokeEntity je WHERE je.hash = :hash ORDER BY je.id")
    List<Long> findIdsByHash(@Param("hash") int hash);

    @Query("SELECT je.hash, je.id  FROM JokeEntity je WHERE je.hash in :hashes ORDER BY je.id")
    List<Object[]> findHashIdPairsByHashesRaw(@Param("hashes") Collection<Integer> hashes);

    @Modifying
    @Query("DELETE FROM JokeEntity je WHERE je.id in :ids")
    void deleteByIdIn(@Param("ids") Collection<Long> ids);

    @Modifying
    default void dropDuplicates() {
        Set<Long> ids = findHashIdMapOfDuplicates().values().stream()
                .map(idsChunk -> idsChunk.stream().skip(1).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        if (!ids.isEmpty()) {
            LOGGER.info("Delete {} duplicates by id", ids.size());

            Chunks.split(ids, 10000)
                    .forEach(this::deleteByIdIn);
        } else {
            LOGGER.info("No duplicates found");
        }
    }

    default Map<Integer, List<Long>> findHashIdMapOfDuplicates() {
        return findHashIdMapByHashes(findDuplicatedHashes());
    }

    default Map<Integer, List<Long>> findHashIdMapByHashes(Collection<Integer> hashes) {
        if (hashes.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, List<Long>> result = new HashMap<>();

        findHashIdPairsByHashesRaw(hashes)
                .forEach(o -> result
                        .computeIfAbsent((Integer) o[0], k -> new ArrayList<>())
                        .add((Long) o[1])
                );

        return result;
    }
}
