package com.afrunt.stupidjokes.jokeservice.rest;

import com.afrunt.stupidjokes.jokeservice.api.Joke;
import com.afrunt.stupidjokes.jokeservice.api.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Andrii Frunt
 */
@RestController
@RequestMapping("/api/joke")
public class JokeController {
    private JokeService jokeService;
    private int bulkMaxSize;

    @Autowired
    public JokeController(JokeService jokeService, @Value("${jokeservice.bulk.maxsize}") int bulkMaxSize) {
        this.jokeService = jokeService;
        this.bulkMaxSize = bulkMaxSize;
    }

    @GetMapping("/random")
    public ResponseEntity<Joke> random() {
        return jokeService.random()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(Map.ofEntries(
                Map.entry("count", jokeService.count())
        ));
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> bulk(@RequestBody List<String> jokes) {
        if (jokes.size() > bulkMaxSize) {
            throw new IllegalArgumentException("Size of the bulk should not be greater than " + bulkMaxSize);
        }
        jokeService.create(jokes);
        return ResponseEntity.ok(null);
    }
}
