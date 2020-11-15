package com.beerhouse.service;

import com.beerhouse.domain.Beer;
import com.beerhouse.repository.BeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Service class for managing Beers.
 */
@Service
@Transactional
public class BeerService {

    private final Logger log = LoggerFactory.getLogger(BeerService.class);
    private final BeerRepository beerRepository;

    BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    /**
     * Verificar se uma cerveja está cadastrada.
     *
     * @param id   beer Id.
     * @param name beer name.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Optional<Beer> existis(Integer id, String name) {
        log.debug("Request to verify if Has Beer Id: {}  Name: {}", id, name);
        return beerRepository.findByIdOrName(id, name);
    }

    /**
     * Get one Beer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Beer> findOne(Integer id) {
        log.debug("Request to get Beer : {}", id);
        return beerRepository.findById(id);
    }

    /**
     * Save a beer.
     *
     * @param beer the entity to save.
     * @return the persisted entity.
     */
    public Beer save(Beer beer) {
        log.debug("Request to save Beer : {}", beer);
        return beerRepository.save(beer);
    }

    /**
     * Delete the beer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Integer id) {
        log.debug("Request to delete Beer : {}", id);
        beerRepository.deleteById(id);
    }

}
