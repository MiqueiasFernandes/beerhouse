package com.beerhouse.repository;

import com.beerhouse.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Beer} entity.
 */
@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {

    Optional<Beer> findById(Integer id);
}
