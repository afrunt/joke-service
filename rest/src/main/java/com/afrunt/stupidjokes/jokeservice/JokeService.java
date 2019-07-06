package com.afrunt.stupidjokes.jokeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author Andrii Frunt
 */
@EntityScan("com.afrunt.stupidjokes.jokeservice.api.impl.entity")
@SpringBootApplication
public class JokeService {
    public static void main(String[] args) {
        SpringApplication.run(JokeService.class, args);
    }
}
