package com.beerhouse.service;

import java.util.List;

import com.beerhouse.domain.Beer_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.beerhouse.domain.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.service.dto.BeerCriteria;

/**
 * Service for executing complex queries for {@link Beer} entities in the database.
 * The main input is a {@link BeerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Beer} or a {@link Page} of {@link Beer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)

public class BeerQueryService extends QueryService<Beer> {
    private final Logger log = LoggerFactory.getLogger(BeerQueryService.class);

    private final BeerRepository beerRepository;

    public BeerQueryService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    /**
     * Return a {@link List} of {@link Beer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Beer> findByCriteria(BeerCriteria criteria) {
        log.debug("find beer by criteria : {}", criteria);
        final Specification<Beer> specification = createSpecification(criteria);
        return beerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Beer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Beer> findByCriteria(BeerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Beer> specification = createSpecification(criteria);
        return beerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BeerCriteria criteria) {
        log.debug("count beers by criteria : {}", criteria);
        final Specification<Beer> specification = createSpecification(criteria);
        return beerRepository.count(specification);
    }

    /**
     * Function to convert {@link BeerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Beer> createSpecification(BeerCriteria criteria) {
        Specification<Beer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Beer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Beer_.name));
            }
            if (criteria.getIngredients() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIngredients(), Beer_.ingredients));
            }
            if (criteria.getAlcoholContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlcoholContent(), Beer_.alcoholContent));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Beer_.price));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), Beer_.category));
            }
        }
        return specification;
    }

}
