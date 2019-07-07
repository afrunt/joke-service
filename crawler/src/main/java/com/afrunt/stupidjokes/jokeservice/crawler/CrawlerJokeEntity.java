package com.afrunt.stupidjokes.jokeservice.crawler;

import javax.persistence.*;

/**
 * @author Andrii Frunt
 */
@Entity
@Table(name = "JOKE")
public class CrawlerJokeEntity {
    @Id
    @SequenceGenerator(name = "JOKE_SEQ_GENERATOR", sequenceName = "JOKE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOKE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "BODY", length = 50000)
    private String body;

    public Long getId() {
        return id;
    }

    public CrawlerJokeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBody() {
        return body;
    }

    public CrawlerJokeEntity setBody(String body) {
        this.body = body;
        return this;
    }

}
