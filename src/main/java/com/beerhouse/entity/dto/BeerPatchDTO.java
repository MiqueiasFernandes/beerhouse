package com.beerhouse.entity.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class BeerPatchDTO {

    @NotNull(message = "O id deve ser informado")
    @Min(value = 1, message = "O id deve ser maior que 0")
    private Integer id;

    @NotNull(message = "O campo price deve ser informado")
    @DecimalMin(value = "0.01", message = "O campo price deve ser no minimo 0.01")
    private Double price;

    public BeerPatchDTO() {
        // Empty constructor needed for Jackson.
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BeerPatchDTO id(Integer id) {
        this.id = id;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BeerPatchDTO price(Double price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "BeerPatchDTO{" +
                "id=" + id +
                ", price=" + price +
                '}';
    }
}
