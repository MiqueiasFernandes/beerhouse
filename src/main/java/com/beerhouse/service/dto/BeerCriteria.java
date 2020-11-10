package com.beerhouse.service.dto;

import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.hibernate.Criteria;

import java.io.Serializable;



/**
 * Criteria class for the {@link com.beerhouse.entity.Beer} entity. This class is used
 * in {@link com.beerhouse.web.rest.BeerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /beers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 *==============> As Spring is unable to properly convert the types, unless specific {@link BeerCriteria} class are used, we need to use
 * fix type specific filters.
 */
public class BeerCriteria {
//    implements
//} Serializable, Criteria {
//
//    private static final long serialVersionUID = 1L;
//
    private IntegerFilter id;

    private StringFilter name;

    private StringFilter ingredients;

    private StringFilter alcoholContent;

    private BigDecimalFilter price;

    private StringFilter category;
//
//    public BeerCriteria(){
//    }
//
//    public BeerCriteria(BeerCriteria other){
//        this.id = other.id == null ? null : other.id.;
//        this.name = other.name == null ? null : other.name.copy();
//        this.value = other.value == null ? null : other.value.copy();
//    }

//    @Override
//    public DBConfCriteria copy() {
//        return new DBConfCriteria(this);
//    }
//
//    public LongFilter getId() {
//        return id;
//    }
//
//    public void setId(LongFilter id) {
//        this.id = id;
//    }
//
//    public StringFilter getName() {
//        return name;
//    }
//
//    public void setName(StringFilter name) {
//        this.name = name;
//    }
//
//    public StringFilter getValue() {
//        return value;
//    }
//
//    public void setValue(StringFilter value) {
//        this.value = value;
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        final DBConfCriteria that = (DBConfCriteria) o;
//        return
//                Objects.equals(id, that.id) &&
//                        Objects.equals(name, that.name) &&
//                        Objects.equals(value, that.value);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(
//                id,
//                name,
//                value
//        );
//    }
//
//    @Override
//    public String toString() {
//        return "DBConfCriteria{" +
//                (id != null ? "id=" + id + ", " : "") +
//                (name != null ? "name=" + name + ", " : "") +
//                (value != null ? "value=" + value + ", " : "") +
//                "}";
//    }

}
