package com.beerhouse.service;

import com.beerhouse.entity.Beer;
import io.github.jhipster.service.QueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for executing complex queries for {@link Beer} entities in the database.
 * The main input is a {@link BeerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Beer} or a {@link Page} of {@link Beer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)

public class BeerQueryService extends QueryService<Beer> {

}
