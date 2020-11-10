package com.beerhouse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table
public class Beer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id = null;

    private String name = null;
    private String ingredients = null;
    private String alcoholContent = null;
    private BigDecimal price = null;
    private String category = null;

    public Beer id(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     * @return id
     **/
    @ApiModelProperty(value = "")


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Beer name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     **/
    @ApiModelProperty(value = "")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Beer ingredients(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    /**
     * Get ingredients
     * @return ingredients
     **/
    @ApiModelProperty(value = "")


    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Beer alcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
        return this;
    }

    /**
     * Get alcoholContent
     * @return alcoholContent
     **/
    @ApiModelProperty(value = "")


    public String getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public Beer price(BigDecimal price) {
        this.price = price;
        return this;
    }

    /**
     * Get price
     * @return price
     **/
    @ApiModelProperty(value = "")

    @Valid

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Beer category(String category) {
        this.category = category;
        return this;
    }

    /**
     * Get category
     * @return category
     **/
    @ApiModelProperty(value = "")


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
        StringBuilder sb = new StringBuilder();
        sb.append("class Beer {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    ingredients: ").append(toIndentedString(ingredients)).append("\n");
        sb.append("    alcoholContent: ").append(toIndentedString(alcoholContent)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}