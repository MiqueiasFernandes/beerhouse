package com.beerhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @Column(length = 50)
    private String alcoholContent;

    @Column(length = 50)
    private String category;

    @JsonIgnore
    @Column(length = 1000)
    private String ingredients;

    @Column(nullable = false)
    private BigDecimal price;

    @ApiModelProperty(value = "Id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ApiModelProperty(value = "Nome")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "Ingredientes")
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @ApiModelProperty(value = "Teor de alcool")
    public String getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    @ApiModelProperty(value = "Preço")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ApiModelProperty(value = "Categoria")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Beer beer = (Beer) o;
        return Objects.equals(this.id, beer.id) &&
                Objects.equals(this.name, beer.name) &&
                Objects.equals(this.ingredients, beer.ingredients) &&
                Objects.equals(this.alcoholContent, beer.alcoholContent) &&
                Objects.equals(this.price, beer.price) &&
                Objects.equals(this.category, beer.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredients, alcoholContent, price, category);
    }

    @Override
    public String toString() {
        return "Beer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alcoholContent='" + alcoholContent + '\'' +
                ", category='" + category + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", price=" + price +
                '}';
    }
}