package com.afrunt.stupidjokes.jokeservice.api;

/**
 * @author Andrii Frunt
 */
public class Joke {
    private Long id;
    private String body;

    public Long getId() {
        return id;
    }

    public Joke setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Joke setBody(String body) {
        this.body = body;
        return this;
    }
}
