package com.afrunt.stupidjokes.jokeservice.api.impl.entity;

import javax.persistence.*;

/**
 * @author Andrii Frunt
 */
@Entity
@Table(name = "JOKE", indexes = {
        @Index(name = "HASH_IDX", columnList = "HASH")
})
public class JokeEntity {
    @Id
    @SequenceGenerator(name = "JOKE_SEQ_GENERATOR", sequenceName = "JOKE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOKE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "BODY", length = 50000)
    private String body;

    @Column(name = "HASH")
    private int hash;

    public Long getId() {
        return id;
    }

    public JokeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBody() {
        return body;
    }

    public JokeEntity setBody(String body) {
        this.body = body;
        return this;
    }

    public int getHash() {
        return hash;
    }

    public JokeEntity setHash(int hash) {
        this.hash = hash;
        return this;
    }
}
