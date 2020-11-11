package com.beerhouse.web.rest;

import com.beerhouse.entity.Beer;
import com.beerhouse.service.BeerQueryService;
import com.beerhouse.service.BeerService;
import com.beerhouse.service.dto.BeerCriteria;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class BeerResource {

    private final Logger log = LoggerFactory.getLogger(BeerResource.class);

    private final BeerService beerService;
    private final BeerQueryService beerQueryService;

    public BeerResource(BeerService beerService, BeerQueryService beerQueryService) {
        this.beerService = beerService;
        this.beerQueryService = beerQueryService;
    }

    /**
     * {@code GET /beers} : get all beers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all beers.
     */
    @GetMapping("/beers")
    @ApiOperation(nickname = "getAllBeers", response = Beer.class, responseContainer = "List", value = "List all beers.")
    public ResponseEntity<List<Beer>> getAllBeers(BeerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Beers page: {}", pageable);
        faik();
        final Page<Beer> page = beerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page
        );
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(page.getContent());
    }

    /**
     * {@code GET  /beers/count} : count all the Beers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/beers/count")
    public ResponseEntity<Long> countBeers(BeerCriteria criteria) {
        log.debug("REST request to count Beer by criteria: {}", criteria);
        return ResponseEntity.ok().body(beerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /beers/:id} : get the "id" Beer.
     *
     * @param id the id of the dBConf to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Beer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beers/{id}")
    @ApiOperation(value = "Get Beer By Id", nickname = "getBeerById", response = Beer.class)
    public ResponseEntity<Beer> getBeer(@PathVariable Integer id) {
        log.debug("REST request to get Beer : {}", id);
        Optional<Beer> dBConf = beerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dBConf);
    }




    void faik() {
        saveBeer(new String[]{"Skol", "100%", "Boa", "Cevada & Alcohol & Água", "3.56"});
        saveBeer(new String[]{"Antartica", "50%", "Otima", "Cevada & Alcohol", "4.9995"});
        saveBeer(new String[]{"Bramaha", "35%", "Boa", "Alcohol & Água", "2"});
    }

    void saveBeer(String[] args) {
        Beer beer = new Beer();
        beer.setName(args[0]);
        beer.setAlcoholContent(args[1]);
        beer.setCategory(args[2]);
        beer.setIngredients(args[3]);
        beer.setPrice(BigDecimal.valueOf(Float.valueOf(args[4])));
        beerService.save(beer);
    }


}
