package com.beerhouse.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface ISort {

    String SEPARADOR = ":";

    String printableSort();

    default List<String[]> fromKeys(String[] keys) {
        return Arrays.stream(keys)
                .filter(key ->
                        StringUtils.isNotBlank(key) &&
                                key.matches("^[a-zA-Z_]+(" + SEPARADOR + "(ASC|DESC|asc|desc))?$"))
                .map(key -> key.split(SEPARADOR))
                .map(key -> new String[]{key[0], key.length > 1 ? key[1].toUpperCase() : "ASC"})
                .collect(Collectors.toList());
    }

}
