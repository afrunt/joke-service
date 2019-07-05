package com.afrunt.stupidjokes.jokeservice.rest;

import com.afrunt.stupidjokes.jokeservice.api.Joke;
import com.afrunt.stupidjokes.jokeservice.api.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
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
        jokeService.create(jokes);
        return ResponseEntity.ok(null);
    }
}
