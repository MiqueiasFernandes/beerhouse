package com.beerhouse.service;

import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Beers.
 */
@Service
@Transactional
public class BeerService {
    private final BeerRepository beerRepository;

    BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public void saveBeer() {
        Beer beer = new Beer();
        beer.setName("Skol");
        beerRepository.save(beer);
    }
}
