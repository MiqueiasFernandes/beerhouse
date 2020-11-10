package com.beerhouse.entity;

import javax.persistence.*;

@Entity
@Table
public class Beer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Beer [name=" + name + "]";
    }
}
