package com.afrunt.stupidjokes.jokeservice.api.impl.repository;

import com.afrunt.stupidjokes.jokeservice.api.impl.entity.JokeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @author Andrii Frunt
 */
@Repository
public interface JokeRepository extends JpaRepository<JokeEntity, Long> {
    @Query("SELECT j.hash FROM JokeEntity j WHERE j.hash in :hashes")
    Set<Integer> findExistingHashes(@Param("hashes") Collection<Integer> hashes);

    @Modifying
    @Query("DELETE FROM JokeEntity")
    void drop();

    @Query("SELECT je.hash FROM JokeEntity je GROUP BY je.hash HAVING count(je) > 1")
    Set<Integer> findDuplicatedHashes();

    @Query("SELECT je.hash, je.id  FROM JokeEntity je WHERE je.hash in :hashes ORDER BY je.id")
    List<Object[]> findHashIdPairsByHashesRaw(@Param("hashes") Collection<Integer> hashes);

    @Modifying
    @Query("DELETE FROM JokeEntity je WHERE je.id in :ids")
    void deleteByIdIn(@Param("ids") Collection<Long> ids);

    default Set<Long> findIdsOfDuplicatesToRemove() {
        return findHashIdMapOfDuplicates().values().stream()
                .map(idsChunk -> idsChunk.stream().skip(1).collect(toList()))
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    default Map<Integer, List<Long>> findHashIdMapOfDuplicates() {
        return findHashIdMapByHashes(findDuplicatedHashes());
    }

    default Map<Integer, List<Long>> findHashIdMapByHashes(Collection<Integer> hashes) {
        if (hashes.isEmpty()) {
            return Collections.emptyMap();
        }

        return findHashIdPairsByHashesRaw(hashes).stream()
                .collect(groupingBy(o -> (Integer) o[0], mapping(o -> (Long) o[1], toList())));
    }
}
