package com.beerhouse.web.rest;

import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.service.BeerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BeerResource {

    private final BeerService beerService; // to db operations
    private final BeerRepository beerRepository; // to complex queryes

    public BeerResource(BeerService beerService, BeerRepository beerRepository) {
        this.beerService = beerService;
        this.beerRepository = beerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeer() {
        this.beerService.saveBeer();
        List<Beer> beers = this.beerRepository.findAll();
        return ResponseEntity.ok(beers);
    }
}
