package com.beerhouse.entity.spring;

import com.beerhouse.entity.ISort;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class SpringSort implements ISort {

    private String[] sort;
    private List<String[]> keys;
    private Sort springSort;

    public SpringSort() {
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
        this.keys = this.fromKeys(sort);
        List<Sort.Order> orders = this.keys
                .stream()
                .map(a -> "ASC".equalsIgnoreCase(a[1]) ? Sort.Order.asc(a[0]) : Sort.Order.desc(a[0]))
                .collect(Collectors.toList());
        this.springSort = Sort.by(orders);
    }

    public Sort getSpringSort() {
        return springSort;
    }

    @Override
    public String printableSort() {
        String uns = "[Unsorted]";
        return this.keys == null ? uns : this.keys
                .stream()
                .map(key -> String.join(SEPARADOR, key))
                .reduce((partialString, element) -> partialString + ";" + element).orElse(uns);

    }

    @Override
    public String toString() {
        return "[Sort " + printableSort() + " ]";
    }
}
