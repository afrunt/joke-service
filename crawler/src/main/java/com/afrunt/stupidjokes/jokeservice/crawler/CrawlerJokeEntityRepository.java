package com.afrunt.stupidjokes.jokeservice.crawler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrii Frunt
 */
public interface CrawlerJokeEntityRepository extends JpaRepository<CrawlerJokeEntity, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM CrawlerJokeEntity")
    void drop();
}
