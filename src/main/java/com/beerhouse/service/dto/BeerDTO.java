package com.beerhouse.service.dto;

import com.beerhouse.domain.Beer;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class BeerDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @Size(max = 50)
    private String alcoholContent;

    @Size(max = 50)
    private String category;

    @Size(max = 50)
    private String ingredients;

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    @Null
    private String formula;

    public BeerDTO() {
        // Empty constructor needed for Jackson.
    }

    public BeerDTO(Beer beer) {
        this.id = beer.getId();
        this.name = beer.getName();
        this.alcoholContent = beer.getAlcoholContent();
        this.category = beer.getCategory();
        this.ingredients = null;
        this.price = beer.getPrice();
        this.formula = beer.getIngredients() == null ?
                "Nenhuma formula cadastrada para essa cerveja." :
                "Há ingredientes conhecidos, consulte o administrador para obter a formula."
        ;
    }

    public Beer toBeer() {
        Beer beer = new Beer();
        beer.setId(getId());
        beer.setName(getName());
        beer.setAlcoholContent(getAlcoholContent());
        beer.setCategory(getCategory());
        beer.setIngredients(getIngredients());
        beer.setPrice(getPrice());
        return beer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BeerDTO id(Integer id) {
        this.id = id;
        return this;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeerDTO name(String name) {
        this.name = name;
        return this;
    }

    public String getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }


    public BeerDTO alcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public BeerDTO category(String category) {
        this.category = category;
        return this;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BeerDTO ingredients(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BeerDTO price(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "BeerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alcoholContent='" + alcoholContent + '\'' +
                ", category='" + category + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", price=" + price +
                ", formula='" + formula + '\'' +
                '}';
    }
}
