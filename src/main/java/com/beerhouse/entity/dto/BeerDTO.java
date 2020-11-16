package com.beerhouse.entity.dto;

import com.beerhouse.domain.Beer;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BeerDTO {

    private Integer id;

    @NotBlank(message = "Um nome valido deve ser informado")
    @Size(min = 3, max = 100, message = "O nome deve possuir no minino 3 e no maximo 100 caracteres.")
    private String name;

    @Size(max = 50, message = "O limite te caracteres é 50")
    private String alcoholContent;

    @Size(max = 50, message = "O limite te caracteres é 50")
    private String category;

    @Size(max = 1000, message = "O limite te caracteres é 1000")
    private String ingredients;

    @NotNull(message = "O preço deve ser informado")
    @DecimalMin(value = "0.01", message = "O preço deve ser no minimo 1 centavo")
    private Double price;

    public BeerDTO() {
        // Empty constructor needed for Jackson.
    }

    public BeerDTO(Beer beer) {
        this.id = beer.getId();
        this.name = beer.getName();
        this.alcoholContent = beer.getAlcoholContent();
        this.category = beer.getCategory();
        this.ingredients = beer.getIngredients();
        this.price = beer.getPrice();
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BeerDTO price(Double price) {
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
                '}';
    }
}
