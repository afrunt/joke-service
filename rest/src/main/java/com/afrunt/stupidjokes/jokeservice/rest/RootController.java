package com.afrunt.stupidjokes.jokeservice.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrii Frunt
 */
@RestController
public class RootController {
    @GetMapping("/")
    public ResponseEntity<Void> index() {
        return ResponseEntity.ok(null);
    }
}
