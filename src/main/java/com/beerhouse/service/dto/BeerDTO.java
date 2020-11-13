package com.beerhouse.service.dto;

import com.beerhouse.domain.Beer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
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

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = name;
    }

    public String getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
