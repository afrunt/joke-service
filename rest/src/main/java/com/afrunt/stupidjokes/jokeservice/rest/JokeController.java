package com.afrunt.stupidjokes.jokeservice.rest;

import com.afrunt.stupidjokes.jokeservice.api.Joke;
import com.afrunt.stupidjokes.jokeservice.api.JokeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @author Andrii Frunt
 */
@RestController
@RequestMapping("/api/joke")
public class JokeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JokeController.class);
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

    @GetMapping("/{id}")
    public ResponseEntity<Joke> findById(@PathVariable("id") long id) {
        return jokeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        StopWatch sw = new StopWatch();
        sw.start();
        long count = jokeService.count();
        sw.stop();
        LOGGER.info("Statistics collected in {}ms", sw.getTotalTimeMillis());
        return ResponseEntity.ok(Map.ofEntries(
                Map.entry("count", count)
        ));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<Void> drop() {
        jokeService.drop();
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/drop-duplicates")
    public ResponseEntity<Void> dropDuplicates() {
        jokeService.dropDuplicates();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> bulk(@RequestBody Set<String> jokes) {
        jokeService.create(jokes);
        return ResponseEntity.ok(null);
    }
}
