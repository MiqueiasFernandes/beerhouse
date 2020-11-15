package com.beerhouse.api.web.rest;

import com.beerhouse.api.BeerAPI;
import com.beerhouse.api.errors.BadRequestProblem;
import com.beerhouse.domain.Beer;
import com.beerhouse.entity.spring.SpringEntity;
import com.beerhouse.entity.spring.SpringPage;
import com.beerhouse.entity.spring.SpringSort;
import com.beerhouse.service.BeerQueryService;
import com.beerhouse.service.BeerService;
import com.beerhouse.service.dto.BeerCriteria;
import com.beerhouse.service.dto.BeerDTO;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


/**
 * Controler Spring para implementar a API em Rest de acesso ao cadastro de {@link com.beerhouse.domain.Beer}.
 * <p>
 * Essa classe implementa as especificações da API em {@link BeerAPI} utilizando Spring.
 */
@RestController
public class BeerSpringAPI implements BeerAPI<
        SpringEntity<List<BeerDTO>>, SpringEntity<Long>, SpringEntity<BeerDTO>,
        SpringPage, SpringSort> {

    private final String ENTITY_NAME = "Beer";
    private final Logger log = LoggerFactory.getLogger(BeerSpringAPI.class);
    private final BeerService beerService;
    private final BeerQueryService beerQueryService;

    public BeerSpringAPI(BeerService beerService, BeerQueryService beerQueryService) {
        this.beerService = beerService;
        this.beerQueryService = beerQueryService;
    }

    private static <T> SpringEntity<T> springEntity(ResponseEntity<T> responseEntity) {
        return SpringEntity.fromResponse(responseEntity);
    }

    private static ResponseEntity.BodyBuilder ok() {
        return ResponseEntity.ok();
    }

    private static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    /**
     * {@code GET  /beers} : Listar todas cervejas.
     *
     * @param page     argumento opcional para paginar o resultado.
     * @param sort     argumento opcional para ordenar o resultado.
     * @param criteria argumento opcional para filtrar o resultado por criterios.
     * @return the {@link SpringEntity} with status {@code 200 (OK)} and the list of {@link BeerDTO} in body.
     */
    @Override
    @GetMapping("/beers")
    public SpringEntity<List<BeerDTO>> getBeers(SpringPage page, SpringSort sort, BeerCriteria criteria) {

        Sort springSort = sort.getSpringSort();
        Pageable pageable = page.getPageable(springSort);

        log.debug("REST request to get {} in page {} of Beers by sort {} and criteria {}",
                page.requestedItems(), page, sort.printableSort(), criteria);

        Page<BeerDTO> beersPage = beerQueryService.findByCriteria(criteria, pageable).map(BeerDTO::new);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                beersPage
        );
        SpringEntity<List<BeerDTO>> response = springEntity(ok().headers(headers).body(beersPage.getContent()));
        log.debug("Consulta de cervejas: {}", response.printableEntity());
        return response;
    }

    /**
     * {@code GET  /beers/count} : Contabilizar cervejas por algum criterio opcional.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link SpringEntity} with status {@code 200 (OK)} and the count as {@link Long} in body.
     */
    @Override
    @GetMapping("/beers/count")
    public SpringEntity<Long> countBeers(BeerCriteria criteria) {
        log.debug("REST request to count Beer by criteria: {}", criteria);
        return springEntity(ok(beerQueryService.countByCriteria(criteria)));
    }

    /**
     * {@code GET  /beers/:id} : Consultar cerveja pelo seu identificador "id".
     *
     * @param id the id of the {@link Beer} to retrieve.
     * @return the {@link SpringEntity} with status {@code 200 (OK)} and with body the {@link BeerDTO},
     * or with status {@code 404 (Not Found)}.
     */
    @Override
    @GetMapping("/beers/{id}")
    public SpringEntity<BeerDTO> getBeer(@PathVariable Integer id) {
        log.debug("REST request to get Beer : {}", id);
        Optional<BeerDTO> beer = beerService.findOne(id).map(BeerDTO::new);
        return springEntity(ResponseUtil.wrapOrNotFound(beer));
    }

    /**
     * {@code POST  /beers} : Cadastrar nova cerveja.
     *
     * @param beer a cerveja a ser cadastrada.
     * @return the {@link SpringEntity} with status {@code 201 (Created)} and with body the new {@link Beer},
     * or with status {@code 400 (Bad Request)} if the {@link Beer} has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PostMapping("/beers")
    public SpringEntity<BeerDTO> postBeer(@RequestBody @Valid BeerDTO beer) throws URISyntaxException {
        log.debug("REST request to save Beer : {}", beer);

        if (beer.getId() != null) {
            throw new BadRequestProblem("O Campo Id deve estar vazio", ENTITY_NAME, "idexists");
        }

        if (beerService.existis(null, beer.getName()).isPresent()) {
            throw new BadRequestProblem("Nome de cerveja ja existe", ENTITY_NAME, "nameexists");
        }

        BeerDTO result = new BeerDTO(beerService.save(beer.toBeer()));
        return springEntity(ResponseEntity
                .created(new URI("/beers/" + result.getId()))
                .body(result));
    }

    /**
     * {@code PUT  /beers} : Atualizar uma cerveja existente.
     *
     * @param beer a cerveja a ser atualizada.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated {@link BeerDTO},
     * or with status {@code 400 (Bad Request)} if the {@link Beer} is not valid,
     * or with status {@code 500 (Internal Server Error)} if the {@link Beer} couldn't be updated.
     */
    @Override
    @PutMapping("/beers")
    public SpringEntity<BeerDTO> putBeer(@RequestBody @Valid BeerDTO beer) {
        log.debug("REST request to update Beer : {}", beer);
        if (beer.getId() == null) {
            throw new BadRequestProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        BeerDTO result = new BeerDTO(beerService.save(beer.toBeer()));
        return springEntity(ok(result));
    }

    /**
     * {@code PATCH  /beers/:id} : Atualizar preço da cerveja pelo seu identificador "id".
     *
     * @param id the id of the {@link Beer} to update price.
     * @return the {@link SpringEntity} with status {@code 200 (OK)} and with body the {@link BeerDTO}.
     */
    @Override
    @PatchMapping("/beers/{id}")
    public SpringEntity<BeerDTO> patchBeer(@PathVariable Integer id, Double price) {
        Beer beer = beerService
                .findOne(id)
                .orElseThrow(() -> new BadRequestProblem("Id invalido", ENTITY_NAME, "invalidId"));

        beer.setPrice(price);
        beerService.save(beer);
        return springEntity(ok(new BeerDTO(beer)));
    }

    /**
     * {@code DELETE  /beers/:id} : Remover a cerveja pelo seu identificador "id".
     *
     * @param id the id of the {@link Beer} to delete.
     * @return the {@link SpringEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @DeleteMapping("/beers/{id}")
    public SpringEntity<Void> deleteBeer(@PathVariable Integer id) {
        log.debug("REST request to delete Beer : {}", id);
        beerService.delete(id);
        return springEntity(ResponseEntity.noContent().build());
    }

}
