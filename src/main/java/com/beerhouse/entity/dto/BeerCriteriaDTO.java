package com.beerhouse.entity.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.beerhouse.domain.Beer} entity. This class is used
 * in {@link com.beerhouse.api.web.rest.BeerSpringAPI} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /beers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BeerCriteriaDTO implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;
    private IntegerFilter id;
    private StringFilter name;
    private StringFilter ingredients;
    private StringFilter alcoholContent;
    private DoubleFilter price;
    private StringFilter category;

    public BeerCriteriaDTO() {
    }

    public BeerCriteriaDTO(BeerCriteriaDTO other) {
        this.id = other.id == null ? null : other.id;
        this.name = other.name == null ? null : other.name.copy();
        this.ingredients = other.ingredients == null ? null : other.ingredients.copy();
        this.alcoholContent = other.alcoholContent == null ? null : other.alcoholContent.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.category = other.category == null ? null : other.category.copy();
    }

    public BeerCriteriaDTO copy() {
        return new BeerCriteriaDTO(this);
    }

    public IntegerFilter getId() {
        return id;
    }

    public void setId(IntegerFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getIngredients() {
        return ingredients;
    }

    public void setIngredients(StringFilter ingredients) {
        this.ingredients = ingredients;
    }

    public StringFilter getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(StringFilter alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public StringFilter getCategory() {
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BeerCriteriaDTO that = (BeerCriteriaDTO) o;
        return
                Objects.equals(id, that.id) &&
                        Objects.equals(name, that.name) &&
                        Objects.equals(ingredients, that.ingredients) &&
                        Objects.equals(alcoholContent, that.alcoholContent) &&
                        Objects.equals(price, that.price) &&
                        Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                ingredients,
                alcoholContent,
                price,
                category
        );
    }

    @Override
    public String toString() {
        return "BeerCriteria{" +
                "id=" + id +
                ", name=" + name +
                ", ingredients=" + ingredients +
                ", alcoholContent=" + alcoholContent +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}
