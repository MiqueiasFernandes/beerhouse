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
    public Optional<Beer> byName(String name) {
        log.debug("Request to verify if Has Beer Name: {}", name);
        return beerRepository.findByName(name);
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
     * Update a beer.
     *
     * @param beer the entity to save.
     * @return the persisted entity.
     */
    public Optional<Beer> update(Beer beer) {
        log.debug("Request to update Beer : {}", beer);
        Optional<Beer> atualizado = beerRepository
                .findById(beer.getId())
                .map(old -> {
                    old.setName(beer.getName());
                    old.setAlcoholContent(beer.getAlcoholContent());
                    old.setCategory(beer.getCategory());
                    old.setIngredients(beer.getIngredients());
                    old.setPrice(beer.getPrice());
                    return old;
                });
        atualizado.ifPresent((beer_) -> beerRepository.save(beer_));
        return atualizado;
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
