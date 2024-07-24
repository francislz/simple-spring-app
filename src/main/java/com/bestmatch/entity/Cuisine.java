package com.bestmatch.entity;

import java.io.Serializable;

public class Cuisine implements Serializable {
    private int id;
    private String name;

    public Cuisine() {
    }

    public Cuisine(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
