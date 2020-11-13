package com.beerhouse.api.web.rest;

import com.beerhouse.api.BeerAPI;
import com.beerhouse.entity.spring.SpringEntity;
import com.beerhouse.entity.spring.SpringPage;
import com.beerhouse.entity.spring.SpringSort;
import com.beerhouse.service.BeerQueryService;
import com.beerhouse.service.BeerService;
import com.beerhouse.service.dto.BeerCriteria;
import com.beerhouse.service.dto.BeerDTO;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Controler Spring para implementar a API em Rest de acesso ao cadastro de {@link com.beerhouse.domain.Beer}.
 * <p>
 * Essa classe implementa as especificações da API em {@link BeerAPI} utilizando Spring.
 */
@RestController
public class BeerSpringAPI implements BeerAPI<SpringPage, SpringSort> {

    private final Logger log = LoggerFactory.getLogger(BeerSpringAPI.class);
    private final BeerService beerService;
    private final BeerQueryService beerQueryService;

    public BeerSpringAPI(BeerService beerService, BeerQueryService beerQueryService) {
        this.beerService = beerService;
        this.beerQueryService = beerQueryService;
    }

    @Override
    @GetMapping("/beers")
    public SpringEntity<List<BeerDTO>> getBeers(SpringPage pagina, SpringSort ordem, BeerCriteria filtro) {

        Sort sort = ordem.getSpringSort();
        Pageable pageable = pagina.getPageable(sort);

        log.debug("REST request {} de pagina {} de Beers na ordem {} filtrado por {}",
                pagina.requestedItems(), pageable, ordem.printableSort(), filtro);

        Page<BeerDTO> page = beerQueryService.findByCriteria(filtro, pageable).map(BeerDTO::new);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page
        );
        ResponseEntity<List<BeerDTO>> response = ResponseEntity.ok().headers(headers).body(page.getContent());
        SpringEntity<List<BeerDTO>> springEntity = SpringEntity.fromResponse(response);
        log.debug("Consulta de cervejas: {}", springEntity.printableEntity());
        return springEntity;
    }

}
