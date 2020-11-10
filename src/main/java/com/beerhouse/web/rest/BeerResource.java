package com.beerhouse.web.rest;

import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.service.BeerService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/beers")
public class BeerResource {

    private final Logger log = LoggerFactory.getLogger(BeerResource.class);

    private final BeerService beerService;       // to operations
    private final BeerRepository beerRepository; // to queryes

    public BeerResource(BeerService beerService, BeerRepository beerRepository) {
        this.beerService = beerService;
        this.beerRepository = beerRepository;
    }

    /**
     * {@code GET /beers} : get all beers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @ApiOperation(nickname = "getAllBeers", response = Beer.class, responseContainer = "List", value = "List all beers.")
    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeers(Pageable pageable) {
        log.debug("REST request to get Beers page: {}", pageable);

        saveBeer(new String[]{"Skol", "100%", "Boa", "Cevada & Alcohol & Água", "3.56"});
        saveBeer(new String[]{"Antartica", "50%", "Otima", "Cevada & Alcohol", "4.9995"});
        saveBeer(new String[]{"Bramaha", "35%", "Boa", "Alcohol & Água", "2"});


        final Page<Beer> page = beerService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
//                ServletUriComponentsBuilder.fromCurrentRequest(),
//                page
//        );
        return ResponseEntity
                .ok()
//                .headers(headers)
                .body(page.getContent());
    }

    public void saveBeer(String[] args) {
        Beer beer = new Beer();
        beer.setName(args[0]);
        beer.setAlcoholContent(args[1]);
        beer.setCategory(args[2]);
        beer.setIngredients(args[3]);
        beer.setPrice(BigDecimal.valueOf(Float.valueOf(args[4])));
        beerService.save(beer);
    }


}
