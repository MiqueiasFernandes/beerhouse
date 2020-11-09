package com.beerhouse.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BeerResource {

    @GetMapping
    public ResponseEntity getAllBeer() {
        return ResponseEntity.ok("Its works!");
    }
}
