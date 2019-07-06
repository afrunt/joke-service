package com.afrunt.stupidjokes.jokeservice.api.impl.repository;

import com.afrunt.stupidjokes.jokeservice.api.impl.entity.JokeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

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
}
